package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Random;

public class AntColony {
	protected boolean USE_CL = false;
	protected AcoType type;
	public Random random;
	private int nr_ants = 0;
	protected TSPData data;
	private int seed;

	private ILS ILS = null;

	public static boolean DEBUG = false;

	private int cnn = -1;

	private Ant[] ants;
	private double[][] pheromone;

	private double p0 = -1;
	private double tau0 = -1;

	private Route bestRoute = null;
	private Route localBest = null;

	private double my_epsilon = 0.4;

	public AntColony(AcoType type, int ants, TSPData data){
		this.type = type;
		initAC((int) (Math.random() * 1000), ants, data);
	}

	public AntColony(AcoType type, int seed, int ants, TSPData data){
		this.type = type;
		initAC(seed, ants, data);
	}

	public void setSolutionImprover(ILS ILS){
		this.ILS = ILS;
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

		this.tau0 = this.p0;

		switch (type){
			case ACS:
				my_epsilon = 1.0 / (this.data.getDimension() * cnn);
				break;
		}

		double[][] iPV = new double[data.getDimension()][data.getDimension()];

		for(int i=0; i<data.getDimension(); i++){
			iPV[i][i] = Double.MAX_VALUE;

			for(int j=i; j<data.getDimension(); j++){
				iPV[i][j] = this.p0;
				iPV[j][i] = this.p0;
			}
		}

		this.pheromone = iPV;

		for(int i=0; i<nr_ants; i++){
			this.ants[i] = new Ant(this);
			this.ants[i].setId(i);
		}
	}

	public double[][] getPheromone() {
		return pheromone;
	}

	public Route run() {
		int runs = 0;

		while(runs < 10 * 1000){

			boolean runEnd = false;
			for(int i=0; i<this.nr_ants; i++){
				this.ants[i].step();
				if(this.ants[i].getStatus() == AntStatus.STOPPED){
					runEnd = true;
				}
			}
			//this.timeTick();

			if(runEnd) {

				Route[] routes = new Route[ants.length];

				if(ILS != null) {
					routes = new Route[ants.length];
					for (int i = 0; i < ants.length; i++) {
						routes[i] = ILS.route(ants[i].getRoute(), data);
					}
				} else {
					for (int i = 0; i < ants.length; i++) {
						routes[i] = ants[i].getRoute();
					}
				}

				for (Route route : routes) {
					if (localBest == null || route.getLength() < localBest.getLength()) {
						localBest = route;
					}
				}


				if(bestRoute == null || localBest.getLength() < bestRoute.getLength()){
					bestRoute = localBest;
				}

				RouteUtils.computePerformance(localBest, data);
				assert(localBest.getLength() >= data.getBestKnown());
				this.globalPheromoneUpdate();

				// Reset ants
				for(Ant ant : ants){
					ant.reset();
				}
				runs++;
			}
		}

		return bestRoute;
	}

	protected double heurN(int i, int j){
		return 1.0 / data.getDistances()[i][j];
	}

	private void globalPheromoneUpdate() {
		// Performed after all ants have completed their tours

		// Global Updating rule (4)
		//timeTick();
		double[][] pv = getPheromone();

		int bestLength = localBest.getLength();

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

						double deltaT;

						if(localBest.hasArc(i, j)){
							deltaT = Math.pow(bestLength, -1);
							pv[i][j] = (1-AntColonySystem.PD) * pv[i][j] +
									AntColonySystem.PD * deltaT;
							//pv[j][i] = pv[i][j];
						}
						break;
				}
			}
		}
	}

	protected void localUpdate(int r, int s){
		/*
			While building a solution (i.e., a tour) of the TSP, ants visit
			edges and change their pheromone level by applying the local
			updating rule of (5)
		 */
		double[][] pv = getPheromone();
		switch(type) {
			case ACS:
				pv[r][s] *= (1-this.my_epsilon);
				pv[r][s] += this.my_epsilon * tau0;
				//pv[s][r] = pv[r][s];
				break;
		}
	}

	public int getSeed() {
		return seed;
	}

	public double getCurrentP(int i, int j) {
		return getPheromone()[i][j];
	}

	public AntColony setCL(boolean cl){
		this.USE_CL = cl;
		return this;
	}
}
