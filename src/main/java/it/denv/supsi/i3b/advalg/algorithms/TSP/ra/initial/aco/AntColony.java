package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;

import java.util.ArrayList;
import java.util.Random;

public class AntColony {
	protected AcoType type;
	public Random random;
	private int nr_ants = 0;
	protected TSPData data;
	private int seed;

	private int cnn = -1;

	private Ant[] ants;
	private ArrayList<double[][]> pheromone = new ArrayList<>();
	private int time = 0;
	private double p0 = -1;

	private Route bestRoute = null;

	public AntColony(AcoType type, int ants, TSPData data){
		this.type = type;
		initAC((int) (Math.random() * 1000), ants, data);
	}

	public AntColony(AcoType type, int seed, int ants, TSPData data){
		this.type = type;
		initAC(seed, ants, data);
	}

	private void initAC(int seed, int ants, TSPData data){
		this.nr_ants = ants;
		this.data = data;
		this.ants = new Ant[this.nr_ants];
		this.random = new Random(seed);
		this.seed = seed;

		this.cnn = new NearestNeighbour(data)
				.route(1, data)
				.getLength();
		this.p0 = (nr_ants * 1.0) / cnn;

		double[][] iPV = new double[data.getDimension()][data.getDimension()];

		for(int i=0; i<data.getDimension(); i++){
			iPV[i][i] = Double.MAX_VALUE;

			for(int j=i; j<data.getDimension(); j++){
				iPV[i][j] = this.p0;
				iPV[j][i] = this.p0;
			}
		}

		this.pheromone.add(iPV);

		for(int i=0; i<nr_ants; i++){
			this.ants[i] = new Ant(this);
		}
	}

	public double[][] getPheromone(int time) {
		return pheromone.get(time);
	}

	public void timeTick(){
		this.time++;
		pheromone.add(time, new double[data.getDimension()][data.getDimension()]);
		System.arraycopy(pheromone.get(time - 1),
				0,
				pheromone.get(time),
				0,
				data.getDimension()
		);
	}

	public Route run() {
		int runs = 0;

		while(runs < 1){

			boolean runEnd = false;
			for(int i=0; i<this.nr_ants; i++){
				this.ants[i].step();
				if(this.ants[i].getStatus() == AntStatus.STOPPED){
					runEnd = true;
				}
			}
			this.timeTick();

			if(runEnd) {
				this.globalPheromoneUpdate();
				// Reset ants
				for(Ant ant : ants){
					ant.reset();
				}
				runs++;
			}
		}

		return null;
	}

	protected double heurN(int i, int j){
		return 1.0 / data.getDistances()[i][j];
	}

	private void globalPheromoneUpdate() {
		// Performed after all ants have completed their tours

		// Global Updating rule (4)
		timeTick();
		double[][] pv = getPheromone(time);
		for(int i=0; i<data.getDimension(); i++){
			for(int j=0; j<data.getDimension(); j++){

				switch(type){
					case ACS:
						/*
							In ACS only the globally best ant (i.e., the ant
							which constructed the shortest tour from the
							beginning of the trial) is allowed to
							deposit pheromone.
						 */

						double deltaT = 0.0;

						if(bestRoute.hasArc(i, j)){
							deltaT = Math.pow(bestRoute.getLength(), -1);
						}

						pv[i][j] = (1-AntColonySystem.PD) * pv[i][j] +
								AntColonySystem.PD * deltaT;
						break;
				}
			}
		}
	}

	private void localUpdate(int r, int s){
		/*
			While building a solution (i.e., a tour) of the TSP, ants visit
			edges and change their pheromone level by applying the local
			updating rule of (5)
		 */
		double[][] pv = getPheromone(time);
		switch(type) {
			case ACS:
				pv[r][s] *= (1-AntColonySystem.PE);
				pv[r][s] += AntColonySystem.PE * p0;
				pv[r][s] = pv[r][s];
				break;
		}
	}

	public int getSeed() {
		return seed;
	}

	public double getCurrentP(int i, int j) {
		return getPheromone(time)[i][j];
	}
}
