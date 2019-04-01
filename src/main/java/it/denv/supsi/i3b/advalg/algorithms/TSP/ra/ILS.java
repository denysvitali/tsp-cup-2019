package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

// Intermediate Routing Algorithm
public interface ILS {
	// Given a Route, improve it
	Route route(Route route, TSPData data);
	int getSeed();
}
