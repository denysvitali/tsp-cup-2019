package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AcoType;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AntColony;

public class AntColonySystem extends RoutingAlgorithm {
	private AntColony ac;


	public AntColonySystem(int seed, int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, ACSParams.getInstance(), seed, amount_ants, data);
	}

	public AntColonySystem(int amount_ants, TSPData data) {
		this.ac = new AntColony(AcoType.ACS, ACSParams.getInstance(), amount_ants, data);
	}

	public AntColonySystem(int seed, int amount_ants, TSPData data,
						   double alpha, double beta, double pd,
						   double pe, double q0){
		this.ac = new AntColony(AcoType.ACS, ACSParams.getInstance(), seed, amount_ants, data);
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
