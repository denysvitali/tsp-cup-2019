package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators.RNNCandidator;

public class RandomNearestNeighbour extends NearestNeighbour {

	public RandomNearestNeighbour(TSPData data) {
		super(data);
		this.candidator = new RNNCandidator(3, data);
	}
}
