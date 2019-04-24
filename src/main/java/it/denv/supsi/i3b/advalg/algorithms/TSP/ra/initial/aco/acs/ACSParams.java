package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.ACOParams;

public class ACSParams implements ACOParams {
	private static ACSParams p = null;
	static double ALPHA = 1;

	// Relative importance of pheromone
	static double BETA = 2;

	// Pheromone Decay (PD, ρ)
	static double PD = 0.1;

	/*
		Pheromone Evaporation (PE, ξ)
	 */
	static double PE = 0.1;
	static double q0 = 0.98;

	public static ACSParams getInstance() {
		if(p == null){
			p = new ACSParams();
		}
		return p;
	}

	@Override
	public double getALPHA() {
		return ALPHA;
	}

	@Override
	public double getBeta() {
		return BETA;
	}

	@Override
	public double getPD() {
		return PD;
	}

	@Override
	public double getPE() {
		return PE;
	}

	@Override
	public double getQ0() {
		return q0;
	}
}
