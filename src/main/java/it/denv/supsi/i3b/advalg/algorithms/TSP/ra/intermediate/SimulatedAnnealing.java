package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Arrays;
import java.util.Random;


public class SimulatedAnnealing implements ILS {

	private static final int r = 100;
	private static final double START_TEMPERATURE = 100.0;
	private int seed;
	private Random random;

	public enum Mode {
		TwoOpt, ThreeOpt, DoubleBridge
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

	private boolean validNumbers(int[] numbers, int maxLength){
		if(numbers[0] < 0){
			return false;
		}

		if(numbers[numbers.length-1] >= maxLength - 1){
			return false;
		}

		for(int i=1; i<numbers.length; i++){
			if(numbers[i] <= numbers[i-1]){
				return false;
			}
		}

		return true;
	}

	private int[] getRandomNumbers(int size, int maxLength){
		int[] numbers = new int[size];
		int r = random.nextInt(maxLength-size);
		int offset = random.nextInt(maxLength - r) / size;

		numbers[0] = r;

		for (int a = 1; a < numbers.length; a++) {
			numbers[a] = numbers[a-1] + offset;
		}

		return numbers;
	}

	private int[] getRandomOffsettedNumbers(int size, int maxLength){
		int[] numbers = new int[size];
		int randomOffset = (int) (random.nextDouble() * (maxLength/size - 1) + 2);
		int prev = 0;
		for (int a = 0; a < numbers.length; a++) {
			numbers[a] = prev + randomOffset;
			prev = numbers[a];
		}

		return numbers;
	}

	@Override
	public Route route(Route route, TSPData data) {
		double temperature = START_TEMPERATURE;

		SwappablePath best = route.getSP();
		SwappablePath current = best;

		TwoOpt twoOpt = new TwoOpt(data);
		ThreeOpt threeOpt = null;

		switch(mode){
			case TwoOpt:
				break;
			case ThreeOpt:
				threeOpt = new ThreeOpt(data);
				break;
		}

		double start = System.currentTimeMillis();

		// TODO: Change me
		double max_runtime = 1000 * 60 * 2 + 1000 * 50; // 2min 50 sec

		while (temperature > 0.2) {
			for (int i = 0; i < r; i++) {
				// 1. n = neighbour(current)

				SwappablePath sp = null;
				SwappablePath improvedSP;

				switch (mode) {
					case TwoOpt:
						int[] twoOptN;
						do{
							twoOptN = getRandomNumbers(2, data.getDim());
						} while(twoOptN[0] == twoOptN[1] || twoOptN[0] > twoOptN[1]
						|| twoOptN[0] == 0);

						sp = current;
						current.twoOptSwap(twoOptN[0], twoOptN[1]);
						break;

					case ThreeOpt:
						int[] threeOptN = getRandomOffsettedNumbers(3, data.getDim());
						sp = current.threeOptSwap(threeOptN[0], threeOptN[1], threeOptN[2])
								[random.nextInt(2)];
						break;

					case DoubleBridge:
						int[] dbN = getRandomOffsettedNumbers(4, data.getDim());
						sp = current.doubleBridge(dbN);
						break;
				}

				assert(sp != null);

				// improvedSP = local_opt(n)

				improvedSP = twoOpt.improveSP(sp);

				assert(improvedSP != null);

				int candidateLength = improvedSP.calculateDistance(data);

				if(candidateLength == data.getBestKnown()){
					// Found our solution
					best = improvedSP;
					break;
				}


				double delta = candidateLength - current.calculateDistance(data);
				double x = random.nextDouble();

				if (candidateLength < current.calculateDistance(data)) {
					current = improvedSP;

					if (current.calculateDistance(data) < best.calculateDistance(data)) {
						best = current;
					}
				} else if (delta < 0 || x < Math.exp(-delta / temperature)) {
					current = improvedSP;
				}
			}


			if(best.getLength() == data.getBestKnown()){
				break;
			}

			long now = System.currentTimeMillis();
			RouteUtils.computePerformance(best, data);

			temperature = START_TEMPERATURE * (1-(now - start)/max_runtime);
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
