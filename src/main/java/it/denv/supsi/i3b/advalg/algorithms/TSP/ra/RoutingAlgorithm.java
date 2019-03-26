package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public abstract class RoutingAlgorithm {
	public abstract Route route(int startNode, TSPData data);
	public abstract int getSeed();
}
