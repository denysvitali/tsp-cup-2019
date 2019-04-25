package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Random;

public class AntColony {
	protected boolean USE_CL = true;
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
	private double[][] choiceInfo; // Combined Pheromone + Heur

	private double p0 = -1;
	private double tau0 = -1;

	private Route globalBest = null;
	private Route localBest = null;

	private ACOParams params;

	public AntColony(AcoType type, ACOParams params, int ants, TSPData data) {
		this.type = type;
		this.params = params;
		initAC((int) (Math.random() * 1000), ants, data);
	}

	public AntColony(AcoType type, ACOParams params, int seed, int ants, TSPData data) {
		this.type = type;
		this.params = params;
		initAC(seed, ants, data);
	}

	public void setSolutionImprover(ILS ILS) {
		this.ILS = ILS;
	}

	private void initAC(int seed, int ants, TSPData data) {
		this.nr_ants = ants;
		this.data = data;
		this.ants = new Ant[this.nr_ants];
		this.random = new Random(seed);
		this.seed = seed;

		this.cnn = new NearestNeighbour(data)
				.route(1, data)
				.getLength();
		this.p0 = 1.0 / (nr_ants * cnn);

		this.tau0 = this.p0;

		switch (type) {
			case ACS:
				break;
		}

		double[][] iPV = new double[data.getDim()][data.getDim()];
		this.choiceInfo = new double[data.getDim()][data.getDim()];

		for (int i = 0; i < data.getDim(); i++) {
			iPV[i][i] = Double.MAX_VALUE;

			for (int j = i; j < data.getDim(); j++) {
				iPV[i][j] = this.p0;
				iPV[j][i] = this.p0;
			}
		}

		int[][] d = data.getDistances();

		for (int i = 0; i < data.getDim(); i++) {
			this.choiceInfo[i][i] = Double.MAX_VALUE;

			for (int j = i; j < data.getDim(); j++) {
				this.choiceInfo[i][j] = iPV[i][j] *
						Math.pow(1.0 / d[i][j], params.getBeta());
				this.choiceInfo[j][i] = this.choiceInfo[i][j];
			}
		}

		this.pheromone = iPV;

		for (int i = 0; i < nr_ants; i++) {
			this.ants[i] = new Ant(this);
			this.ants[i].setId(i);
		}
	}

	public double[][] getPheromone() {
		return pheromone;
	}

	public Route run() {
		int runs = 0;

		long start = System.currentTimeMillis();
		long expectedEnd = start + 1000 * 60 * 2 + 1000 * 50; // 2 Minutes and 50 seconds

		while (System.currentTimeMillis() < expectedEnd && (globalBest == null ||
				globalBest.getLength() != data.getBestKnown())) {

			boolean runEnd = false;
			for (int i = 0; i < this.nr_ants; i++) {
				this.ants[i].step();
				if (this.ants[i].getStatus() == AntStatus.STOPPED) {
					runEnd = true;
				}
			}

			if (runEnd) {
				Route[] routes = new Route[ants.length];

				if (ILS != null) {
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


				if (globalBest == null || localBest.getLength() < globalBest.getLength()) {
					globalBest = localBest;
				}

				if(runs % 100 == 0){
					System.out.println("Iter " + runs);
					RouteUtils.computePerformance(localBest, data);
				}
				assert (localBest.getLength() >= data.getBestKnown());

				//this.pheromoneEvaporation();
				this.globalPheromoneUpdate();

				// Reset ants
				for (Ant ant : ants) {
					ant.reset();
				}
				runs++;
			}
		}

		return globalBest;
	}

	private void pheromoneEvaporation() {
		for (int i = 0; i < data.getDim(); i++) {
			for (int j = i; j < data.getDim(); j++) {
				pheromone[i][j] = (1.0 - params.getPD()) * pheromone[i][j]
				+ this.p0;
				pheromone[j][i] = pheromone[i][j];
			}
		}
	}

	protected double heurN(int i, int j) {
		return 1.0 / data.getDistances()[i][j];
	}

	private void globalPheromoneUpdate() {
		// Performed after all ants have completed their tours

		// Global Updating rule (4)
		//timeTick();
		double[][] pv = getPheromone();
		double[][] ci = getChoiceInfo();
		int[][] d = data.getDistances();

		int bestLength = globalBest.getLength();

		switch (type) {
			case ACS:
				/*
					In ACS only the globally best ant (i.e., the ant
					which constructed the shortest tour from the
					beginning of the trial) is allowed to
					deposit pheromone.
				 */

				int[] bestPath = globalBest.getPath();
				for (int i = 0; i < bestPath.length - 1; i++) {
					double deltaT = Math.pow(bestLength, -1);
					int p = bestPath[i];
					int q = bestPath[i+1];
					pv[p][q] = (1 - params.getPD()) * pv[p][q] + params.getPD() * deltaT;
					pv[q][p] = pv[p][q];

					ci[p][q] = pv[p][q] * Math.pow(1.0 / d[p][q], params.getBeta());
					ci[q][p] = ci[p][q];
				}
		}
	}

	double[][] getChoiceInfo() {
		return this.choiceInfo;
	}

	double getChoiceInfo(int i, int j) {
		return this.choiceInfo[i][j];
	}

	protected void localUpdate(int r, int s) {
		/*
			While building a solution (i.e., a tour) of the TSP, ants visit
			edges and change their pheromone level by applying the local
			updating rule of (5)
		 */
		double[][] pv = getPheromone();
		double[][] ci = getChoiceInfo();
		int[][] d = data.getDistances();
		switch (type) {
			case ACS:
				pv[r][s] *= (1-params.getPE());
				pv[r][s] += params.getPE() * tau0;
				pv[s][r] = pv[r][s];

				ci[r][s] = pv[r][s] * Math.pow(1.0 / d[r][s], params.getBeta());
				ci[s][r] = ci[r][s];
				break;
		}
	}

	public int getSeed() {
		return seed;
	}

	public double getCurrentP(int i, int j) {
		return getPheromone()[i][j];
	}

	public AntColony setCL(boolean cl) {
		this.USE_CL = cl;
		return this;
	}

	public ACOParams getParams() {
		return params;
	}
}
