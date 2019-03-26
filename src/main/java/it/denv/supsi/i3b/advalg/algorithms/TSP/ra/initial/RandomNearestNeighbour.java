package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators.RNNCandidator;

public class RandomNearestNeighbour extends NearestNeighbour {

	public RandomNearestNeighbour(int seed, TSPData data){
		super(data);
		this.candidator = new RNNCandidator(seed, 10, data);
		System.out.println(this.getClass().getName() + "'s seed is " +  seed);
	}

	public RandomNearestNeighbour(TSPData data) {
		super(data);
		this.candidator = new RNNCandidator(10, data);
		System.out.println(this.getClass().getName() + "'s seed is " +
				((RNNCandidator) this.candidator).getSeed());
	}
}
