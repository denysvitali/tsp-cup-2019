package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.NeighbourAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.neighbour.RandomSwap;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;


public class SimulatedAnnealing implements IntermediateRoutingAlgorithm {

	private static final int r = 100;
	private NeighbourAlgorithm na = new RandomSwap(5);
	private static final double START_TEMPERATURE = 100.0;
	private int seed = -1;

	public SimulatedAnnealing(int seed){
		this.seed = seed;
	}

	public SimulatedAnnealing(){
	}

	/*
	procedure simulated annealing
		(1) Compute an initial tour T and choose an initial temperature θ > 0
		 	and a repetition factor r.
		(2) As long as the stopping criterion is not satisfied perform
			the following steps:
		(2.1) Do the following r times.
		(2.1.1) Perform a random modification of the current tour to obtain
				the tour T' and let ∆ = c(T') − c(T) (difference of lengths).
		(2.1.2) Compute a random number x, 0 ≤ x ≤ 1.
		(2.1.3) If ∆ < 0 or x < exp(−∆/θ) then set T = T'.
		(2.2) Update θ and r.
		(3) Output the current tour T as solution.
	end of simulated annealing
	 */

	@Override
	public Route route(Route route, TSPData data) {
		double temperature = START_TEMPERATURE;

		Route best = route;
		Route current = route;

		TwoOpt twoOpt = new TwoOpt(data);

		double start = System.currentTimeMillis();
		double max_runtime = 1000 * 60 * 2;

		while(temperature > 0.001){
			for(int i=0; i<r; i++){
				// 1. n = neighbour(current)

				int i_1 = 0;
				int j_1 = 0;

				do {
					i_1 = (int) (Math.random() * data.getDimension());
				}
				while(i_1 == 0 || i_1 == data.getDimension());


				do {
					j_1 = (int) (Math.random() * data.getDimension());
				}
				while(j_1 == 0 || j_1 == data.getDimension());

				SwappablePath sp = new SwappablePath(current.getPath())
						.twoOptSwap(i_1, j_1);
				// 2. candidators = local_opt(n)

				int[] improvedSP = twoOpt.improveSP(sp);
				int candidateLength = new SwappablePath(improvedSP)
						.calulateDistance(data);


				double delta = candidateLength - current.getLength();
				double x = Math.random();

				if(candidateLength < current.getLength()){
					current = new Route(improvedSP, data);

					if(current.getLength() < best.getLength()){
						best = current;
					}
				} else if(delta < 0 || x < Math.exp(-delta / temperature)){
					current = new Route(improvedSP, data);
				}
			}

			double now = System.currentTimeMillis();

			System.out.println("Spent " + (now - start) + "ms");
			System.out.println("Current Length: " + current + " / " + data.getBestKnown());

			RouteUtils.computePerformance(current, data);

			temperature = START_TEMPERATURE * (1 - (now-start)/max_runtime);
			System.out.println("Temperature is " + temperature);
		}


		return best;

	}
}
