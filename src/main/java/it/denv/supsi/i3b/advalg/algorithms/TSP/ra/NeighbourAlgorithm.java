package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.Route;

public interface NeighbourAlgorithm {
	Route computeNeighbour(Route r);
}
