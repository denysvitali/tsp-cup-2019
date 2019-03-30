package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IRA;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class AntColony {
	protected AcoType type;
	public Random random;
	private int nr_ants = 0;
	protected TSPData data;
	private int seed;

	private IRA ira = null;

	public static boolean DEBUG = false;

	private int cnn = -1;

	private Ant[] ants;
	private ArrayList<double[][]> pheromone = new ArrayList<>();
	private int time = 0;
	private double p0 = -1;

	private Route bestRoute = null;
	private Route localBest = null;

	private double my_epsilon = -1;

	public AntColony(AcoType type, int ants, TSPData data){
		this.type = type;
		initAC((int) (Math.random() * 1000), ants, data);
	}

	public AntColony(AcoType type, int seed, int ants, TSPData data){
		this.type = type;
		initAC(seed, ants, data);
	}

	public void setSolutionImprover(IRA ira){
		this.ira = ira;
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

		switch (type){
			case ACS:
				my_epsilon = 1.0 / (this.data.getDimension() * this.cnn);
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

		this.pheromone.add(iPV);

		for(int i=0; i<nr_ants; i++){
			this.ants[i] = new Ant(this);
			this.ants[i].setId(i);
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

		while(runs < 1000){

			boolean runEnd = false;
			for(int i=0; i<this.nr_ants; i++){
				this.ants[i].step();
				if(this.ants[i].getStatus() == AntStatus.STOPPED){
					runEnd = true;
				}
			}
			this.timeTick();

			if(runEnd) {

				ArrayList<Route> routes = new ArrayList<>();
				for(Ant ant : ants){
					routes.add(
							ant.getRoute()
					);
				}

				if(ira != null){
					ArrayList<Route> improvedRoutes = new ArrayList<>();
					for(Route r : routes){
						improvedRoutes.add(ira.route(r, data));
					}
					routes.addAll(improvedRoutes);
				}

				Optional<Route> r = routes.stream()
						.sorted(Route::compare).limit(1).findAny();

				if(r.isPresent()){
					localBest = r.get();

					if(bestRoute == null || localBest.getLength() < bestRoute.getLength()){
						bestRoute = r.get();
					}
					RouteUtils.computePerformance(localBest, data);
					this.globalPheromoneUpdate();
				}

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

						if(localBest.hasArc(i, j)){
							deltaT = Math.pow(localBest.getLength(), -1);
						}

						pv[i][j] = (1-AntColonySystem.PD) * pv[i][j] +
								AntColonySystem.PD * deltaT;
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
		double[][] pv = getPheromone(time);
		switch(type) {
			case ACS:
				pv[r][s] *= (1-this.my_epsilon);
				pv[r][s] += this.my_epsilon * p0;
				pv[s][r] = pv[r][s];
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
