package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IRA;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AcoType;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AntColony;

public class AntColonySystem extends RoutingAlgorithm {
	// Relative importance of heuristic
	public static final double ALPHA = 0.1;

	// Relative importance of pheromone
	public static final double BETA = 1.0;

	// Pheromone Decay (PD)
	public static final double PD = 0.2;

	// Pheromone Evaporation (PE)
	public static final double PE = 0.1;

	public static final double q0 = 0.5;

	private AntColony ac;
	private IRA ira = null;


	public AntColonySystem(int seed, int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, seed, amount_ants, data);
	}

	public AntColonySystem(int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, amount_ants, data);
	}

	public AntColonySystem setSolutionImprover(IRA ira){
		this.ira = ira;
		return this;
	}

	@Override
	public Route route(int startNode, TSPData data) {
		Route r = this.ac.run();
		return r;
	}

	@Override
	public int getSeed() {
		return this.ac.getSeed();
	}
}
