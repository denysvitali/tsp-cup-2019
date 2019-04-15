package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators.RNNCandidator;

import java.util.Random;

public class RandomNearestNeighbour extends NearestNeighbour {
	private Random r;
	private int size = 10;
	private TSPData data;

	public RandomNearestNeighbour(int seed, TSPData data){
		super(data);
		this.r = new Random(seed);
		this.data = data;
		System.out.println(this.getClass().getName() + "'s seed is " +  seed);
		reset();
	}

	public RandomNearestNeighbour(Random r, TSPData d){
		super(d);
		this.r = r;
		this.data = d;
		reset();
	}

	public RandomNearestNeighbour(TSPData data) {
		super(data);
		this.data = data;
		this.r = new Random();
		System.out.println(this.getClass().getName() + "'s seed is " +
				((RNNCandidator) this.candidator).getSeed());
		reset();
	}

	@Override
	public void reset() {
		super.reset();
		this.candidator = new RNNCandidator(r, size, data);
	}
}
