package it.denv.supsi.i3b.advalg.ra;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TestShuffler extends RoutingAlgorithm {
	private Random rand;
	private int seed;

	public TestShuffler(int seed){
		this.seed = seed;
		this.rand = new Random(seed);
	}

	@Override
	public Route route(int startNode, TSPData data) {
		ArrayList<Integer> path = new ArrayList<>();

		for(int i=0; i<data.getDimension(); i++){
			path.add(i);
		}

		Collections.shuffle(path, this.rand);
		path.add(path.get(0));

		int[] newPath = path.stream().mapToInt(Integer::intValue).toArray();

		assert(newPath.length == data.getDimension() + 1);

		Route r = new Route(newPath, data);
		return r;
	}

	@Override
	public int getSeed() {
		return seed;
	}
}
