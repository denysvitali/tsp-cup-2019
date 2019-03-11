package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.ArrayList;
import java.util.List;

public class AntColony {
	private ArrayList<Ant> ants = new ArrayList<>();
	private TSPData data;

	private double[][] pheromoneValues;
	private double[][] initialPheromoneValues;


	// Decay Parameter:
	public final double ALPHA = 0.1; // 0 < a < 1
	// Relative importance of pheromone vs. distance
	public final double BETA = 0.3; // Must be >0
	// Probability of a biased exploration
	public final double Q0 = 0.3; // 0 < Q0 < 1

	// Local Trail Decay
	public final double PL = 0.1;

	private double iPV = 1;

	public AntColony(int nrAnts, TSPData data){
		assert(BETA > 0);

		this.data = data;

		for(int i=0; i<nrAnts; i++){
			this.ants.add(new Ant(this));
		}

		iPV = iPV / data.getBestKnown();

		pheromoneValues = new double[data.getDimension()][data.getDimension()];
		initialPheromoneValues = new double[data.getDimension()][data.getDimension()];

		for(int i=0; i<data.getDimension(); i++){
			for(int j=0; j<i; j++){
				initialPheromoneValues[i][j] = iPV;
				pheromoneValues[i][j] = iPV;

				initialPheromoneValues[j][i] = iPV;
				pheromoneValues[j][i] = iPV;
			}
		}
	}

	public double getiPV() {
		return iPV;
	}

	public double getPheromone(int i, int j){
		return pheromoneValues[i][j];
	}

	public int sizeNodes(){
		return data.getDimension();
	}

	public int getTarget(){
		return data.getStartNode();
	}

	public double getBeta() {
		return BETA;
	}

	public void updatePheronome(){
		for(int i=0; i<sizeNodes(); i++){
			for(int j=0; j<i; j++){
				double sumDeltaTk = 0.0;

				for(Ant ant : ants){
					if(ant.hasArc(i, j)){
						sumDeltaTk += 1/ant.pathLength();
					}
				}

				pheromoneValues[i][j] =
						(1-ALPHA) * pheromoneValues[i][j] +
								sumDeltaTk;
			}
		}
	}

	public double getN(int i, int k) {
		// n = 1/d, where d = distance
		return 1.0 / data.getDistances()[i][k];
	}

	public double[][] getPheromoneValues() {
		return pheromoneValues;
	}

	public int[][] getDistances() {
		return data.getDistances();
	}

	public double getAlpha() {
		return ALPHA;
	}

	public double getInitialPheromoneValue(int i, int j) {
		return initialPheromoneValues[i][j];
	}

	public double getQ0() {
		return Q0;
	}

	public List<Ant> getAnts() {
		return ants;
	}

	public int getStartNode() {
		return data.getStartNode();
	}

	public void setPheromone(int i, int j, double v) {
		pheromoneValues[i][j] = v;
	}

	public void globalUpdate(Route route) {
		int[] p = route.getPath();

		for(int i = 0; i<p.length-1; i++){
			pheromoneValues[p[i]][p[i+1]] -= 0.1;
		}
	}

	public TSPData getData() {
		return data;
	}
}
