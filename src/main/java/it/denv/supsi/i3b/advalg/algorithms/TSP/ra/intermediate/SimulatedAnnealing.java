package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Random;


public class SimulatedAnnealing implements ILS {

	private static final int r = 100;
	private static final double START_TEMPERATURE = 160.0;
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

		SwappablePath best = null;
		SwappablePath current = route.getSP();

		TwoOpt twoOpt = new TwoOpt(data);
		twoOpt.setCandidate(true);
		ThreeOpt threeOpt = null;

		switch(mode){
			case TwoOpt:
				break;
			case ThreeOpt:
				threeOpt = new ThreeOpt(data);
				break;
		}

		double start = System.currentTimeMillis();

		double max_runtime = 1000 * 60 * 2 + 1000 * 50; // 2min 50 sec
		int iter = 0;

		int originalLength = route.getLength();
		double originalPercentage = 1 - data.getBestKnown() * 1.0 / originalLength;

		double alpha = 0.95;
		double sqrt_n = Math.sqrt(data.getDim());

		int new_r = 100;
		System.out.println("new_r = " + new_r);

		while (temperature > 1E-10 && System.currentTimeMillis() <= max_runtime + start) {

			//double t_cool = Math.pow(alpha, iter);
			double t_cool = alpha;

			for (int i = 0; i < new_r; i++) {
				// 1. n = neighbour(current)

				SwappablePath sp = null;
				SwappablePath nextSP = null;

				switch (mode) {
					/*case TwoOpt:
						int[] twoOptN;
						do{
							twoOptN = getRandomNumbers(2, data.getDim());
						} while(twoOptN[0] == twoOptN[1] || twoOptN[0] > twoOptN[1]
						|| twoOptN[0] == 0);

						sp = new SwappablePath(current);
						sp.twoOptSwap(twoOptN[0], twoOptN[1]);
						break;

					case ThreeOpt:
						int[] threeOptN = getRandomOffsettedNumbers(3, data.getDim());
						sp = current.threeOptSwap(threeOptN[0], threeOptN[1], threeOptN[2])
								[random.nextInt(2)];
						break;*/

					case DoubleBridge:
						int[] dbN = getRandomOffsettedNumbers(4, data.getDim());
						sp = current.doubleBridge(dbN);
						break;
				}

				assert(sp != null);

				// improvedSP = local_opt(n)

				nextSP = twoOpt.improveSP(new SwappablePath(sp));
				assert(nextSP != null);

				int nextLength = nextSP.calculateDistance(data);
				int bestLength = (best != null ? best.getLength() : Integer.MAX_VALUE);
				int currentLength = current.calculateDistance(data);

				assert(bestLength != -1);

				if(nextLength == data.getBestKnown()){
					// Found our solution
					best = nextSP;
					break;
				}

				double delta = nextLength - currentLength;
				double x = random.nextDouble();

				if (currentLength < nextLength) {
					current = new SwappablePath(nextSP);

					if (currentLength < bestLength) {
						best = new SwappablePath(nextSP);
					}
				}
				else if (delta == 0 || x < Math.exp(-delta / temperature)) {
					current = new SwappablePath(nextSP);
				}
			}


			if(best.getLength() == data.getBestKnown()){
				break;
			}

			RouteUtils.computePerformance(best, data);

			//temperature = START_TEMPERATURE * (1-(now - start)/max_runtime);
			// temperature decreases as the performance does

			//double percentage = 1 - data.getBestKnown() * 1.0 / best.getLength();


			//double alpha = 1 + 1 * Math.log(1 + iter);
			//double logval = Math.log(1 + originalPercentage / percentage * 2 + iter * 1.0);
			temperature *= t_cool;

			iter++;
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
