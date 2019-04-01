package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.NeighbourAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.neighbour.RandomSwap;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Arrays;
import java.util.Random;


public class SimulatedAnnealing implements ILS {

	private static final int r = 100;
	private static final double START_TEMPERATURE = 100.0;
	private int seed;
	private Random random;

	public enum Mode {
		TwoOpt, ThreeOpt
	}

	private Mode mode = Mode.TwoOpt;

	public SimulatedAnnealing(int seed) {
		this.seed = seed;
		this.random = new Random(seed);
	}

	public SimulatedAnnealing() {
		this.seed = (int) (Math.random() * 10000);
		this.random = new Random(this.seed);
		System.out.println(this.getClass().getName() + " - Seed: " + seed);
	}

	public SimulatedAnnealing setMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public Mode getMode() {
		return mode;
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

		SwappablePath best = route.getSP();
		SwappablePath current = best;

		TwoOpt twoOpt = null;
		ThreeOpt threeOpt = null;

		switch(mode){
			case TwoOpt:
				twoOpt = new TwoOpt(data);
				break;
			case ThreeOpt:
				threeOpt = new ThreeOpt(data);
				break;
		}

		double start = System.currentTimeMillis();
		double max_runtime = 1000 * 60 * 2;

		while (temperature > 0.001) {
			for (int i = 0; i < r; i++) {
				// 1. n = neighbour(current)

				SwappablePath sp;
				SwappablePath improvedSP = null;

				switch (mode) {
					case TwoOpt:
						int i_1;
						int j_1;

						do {
							i_1 = random.nextInt(data.getDimension());
						}
						while (i_1 == 0 || i_1 == data.getDimension());


						do {
							j_1 = random.nextInt(data.getDimension());
						}
						while (j_1 == 0 || j_1 == i_1 || j_1 == data.getDimension());

						sp = current.twoOptSwap(i_1, j_1);

						// 2. candidators = local_opt(n)

						switch(mode){
							case TwoOpt:
								assert twoOpt != null;
								improvedSP = twoOpt.improveSP(sp);
								break;
							case ThreeOpt:
								assert threeOpt != null;
								improvedSP = threeOpt.improveSP(sp);
								break;
						}
						break;

					case ThreeOpt:
						int[] numbers = new int[3];

						do {
							for (int a = 0; a < numbers.length; a++) {
								numbers[a] = (int) (random.nextDouble() * data.getDimension());
							}
							Arrays.sort(numbers);
						} while (
								numbers[0] == 0 || numbers[2] >= data.getDimension()
								|| numbers[0] + 1 == numbers[1]
								|| numbers[1] + 1 == numbers[2]);

						improvedSP = current.threeOptSwap(numbers[0], numbers[1], numbers[2])[random.nextInt(2)];
						//improvedSP = twoOpt.improveSP(sp);

						break;
				}

				assert(improvedSP != null);

				int candidateLength = improvedSP.calulateDistance(data);

				if(candidateLength == data.getBestKnown()){
					// Found our solution
					best = improvedSP;
					break;
				}


				double delta = candidateLength - current.getLength();
				double x = random.nextDouble();

				if (candidateLength < current.getLength()) {
					current = improvedSP;

					if (current.getLength() < best.getLength()) {
						best = current;
					}
				} else if (delta < 0 || x < Math.exp(-delta / temperature)) {
					current = improvedSP;
				}
			}

			RouteUtils.computePerformance(current, data);
			long now = System.currentTimeMillis();

			temperature = START_TEMPERATURE * (1 - (now - start) / max_runtime);
			System.out.println("Temperature is " + temperature);
		}

		System.out.println(seed);


		return new Route(best, data);

	}

	@Override
	public int getSeed() {
		return seed;
	}
}
