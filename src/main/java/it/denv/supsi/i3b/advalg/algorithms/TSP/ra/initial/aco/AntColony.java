package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.ArrayList;

public class AntColony {
	private ArrayList<Ant> ants = new ArrayList<>();
	private TSPData data;

	private double[][] pheromoneValues;


	// Decay Parameter:
	private final double ALPHA = 0.4; // 0 < a < 1

	// Relative importance of pheromone vs. distance
	private final double BETA = 0.1; // Must be >0

	public AntColony(int nrAnts, TSPData data){
		assert(BETA > 0);

		this.data = data;

		for(int i=0; i<nrAnts; i++){
			this.ants.add(new Ant(this));
		}

		pheromoneValues = new double[data.getDimension()][data.getDimension()];
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

	public int[][] getDistances() {
		return data.getDistances();
	}
}
