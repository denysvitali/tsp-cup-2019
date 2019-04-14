package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AcoType;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AntColony;

public class AntColonySystem extends RoutingAlgorithm {
	// Relative importance of heuristic
	public static final double ALPHA = 1;

	// Relative importance of pheromone
	public static final double BETA = 3.2;

	// Pheromone Decay (PD, ρ)
	public static final double PD = 0.2;

	/*
		Pheromone Evaporation (PE, ξ)
		==============================
		Since this value is calculated dynamically,
		this value is IGNORED!

		Refer to AntColony.java, specifically the my_epsilon
		parameter. Tweaking this won't affect the algorithm
	 */
	public static final double UNUSED_PE = -1;

	public static final double q0 = 0.97;

	private AntColony ac;
	private ILS ILS = null;


	public AntColonySystem(int seed, int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, seed, amount_ants, data);
	}

	public AntColonySystem(int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, amount_ants, data);
	}

	public AntColonySystem setSolutionImprover(ILS ILS){
		this.ILS = ILS;
		this.ac.setSolutionImprover(ILS);
		return this;
	}

	@Override
	public Route route(int startNode, TSPData data) {
		return this.ac.run();
	}

	@Override
	public int getSeed() {
		return this.ac.getSeed();
	}
}
