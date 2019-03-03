package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.util.ArrayList;

public class AntColonyOptimization extends RoutingAlgorithm {
	private int amount_ants;
	private AntColony ac;

	public AntColonyOptimization(int amount_ants, TSPData data){
		this.ac = new AntColony(amount_ants, data);
	}

	@Override
	public Route route(int startNode, TSPData data) {
		return null;
	}
}
