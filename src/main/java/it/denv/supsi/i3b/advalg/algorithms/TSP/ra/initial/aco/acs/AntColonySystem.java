package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AcoType;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AntColony;

public class AntColonySystem extends RoutingAlgorithm {
	// Relative importance of heuristic
	public double ALPHA = 1;

	// Relative importance of pheromone
	public double BETA = 2;

	// Pheromone Decay (PD, ρ)
	public double PD = 0.1;

	/*
		Pheromone Evaporation (PE, ξ)
	 */
	public double PE = 0.1;
	public double q0 = 0.98;

	private AntColony ac;


	public AntColonySystem(int seed, int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, seed, amount_ants, data);
		ac.ALPHA = ALPHA;
		ac.BETA = BETA;
		ac.PD = PD;
		ac.PE = PE;
		ac.q0 = q0;
	}

	public AntColonySystem(int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, amount_ants, data);
		ac.ALPHA = ALPHA;
		ac.BETA = BETA;
		ac.PD = PD;
		ac.PE = PE;
		ac.q0 = q0;
	}

	public AntColonySystem(int seed, int amount_ants, TSPData data,
						   double alpha, double beta, double pd,
						   double pe, double q0){
		this.ac = new AntColony(AcoType.ACS, seed, amount_ants, data);
		ac.ALPHA = alpha;
		ac.BETA = beta;
		ac.PD = pd;
		ac.PE = pe;
		ac.q0 = q0;
	}

	public AntColonySystem setSolutionImprover(ILS ILS){
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
