package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Random;


public class SimulatedAnnealing implements ILS {

	private static final int r = 1000;
	private static final double START_TEMPERATURE = 180.0;
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
		current.calculateDistance(data);

		TwoOpt twoOpt = new TwoOpt(data);
		twoOpt.setCandidate(false);

		int length = current.getPathArr().length;
		int bestKnown = data.getBestKnown();

		double originalPerc = 1 - bestKnown*1.0 / current.getLength();
		System.out.println(originalPerc);

		RouteUtils.computePerformance(current, data);

		int iter = 0;
		double currentPerc = -1;
		while(temperature > 1E-9){

			if(best == null){
				currentPerc = originalPerc;
			}

			double alpha = 0.996;

			for(int i=0; i<r; i++){
				SwappablePath next = null;

				switch(mode){
					case DoubleBridge:
						next = current.doubleBridge(
								getRandomOffsettedNumbers(4, length-1)
						);
						break;
				}

				assert(next != null);

				next = twoOpt.improveSP(next);
				next.calculateDistance(data);

				assert(next.getLength() != -1);
				assert(current.getLength() != -1);

				int delta = next.getLength() - current.getLength();

				double x = random.nextDouble();
				if(best == null){
					best = current;
				}

				if(best.getLength() == data.getBestKnown()) {
					break;
				}

				if(delta < 0){
					current = next;
					if(current.getLength() < best.getLength()){
						best = current;
						currentPerc = 1 - bestKnown * 1.0 / best.getLength();
						iter = 0;
					}
				}

				if(x < Math.exp(-delta * 1.0/temperature)){
					current = next;
				}
			}

			System.out.println("Temperature is " + temperature);
			RouteUtils.computePerformance(best, data);
			//temperature *= Math.pow(alpha, iter);
			temperature *= alpha;

			iter++;
		}


		return new Route(best, data);

	}

	@Override
	public int getSeed() {
		return seed;
	}
}
