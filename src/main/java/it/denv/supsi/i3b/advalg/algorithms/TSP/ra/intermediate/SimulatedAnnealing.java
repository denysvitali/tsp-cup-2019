package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.NeighbourAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.neighbour.RandomSwap;

public class SimulatedAnnealing implements IntermediateRoutingAlgorithm {

	private static final int r = 100;
	private NeighbourAlgorithm na = new RandomSwap(3);
	private static final double START_TEMPERATURE = 100.0;

	@Override
	public Route route(Route route, TSPData data) {
		double temperature = START_TEMPERATURE;

		Route best = route;
		Route current = route;
		Route candidate = null;

		int best_length = Integer.MAX_VALUE;
		TwoOpt twoOpt = new TwoOpt(data);

		double start = System.currentTimeMillis();
		double end = start + 1000 * 60 * 3;

		while(temperature > 0.001){
			for(int i=0; i<r; i++){
				// 1. n = neighbour(current)

				Route n = na.computeNeighbour(current);

				// 2. candidate = local_opt(n)

				candidate = twoOpt.improve(n);

				double delta = candidate.getLength() - current.getLength();
				double x = Math.random();

				if(candidate.getLength() < current.getLength()){
					current = candidate;

					if(current.getLength() < best.getLength()){
						best = current;
					}
				}  else if(delta < 0 || x < Math.exp(-delta / temperature)){
					current = candidate;
				}
			}

			temperature = START_TEMPERATURE * (1 - System.currentTimeMillis()/end);
			System.out.println("Temperature is " + temperature);
		}


		return best;

	}
}
