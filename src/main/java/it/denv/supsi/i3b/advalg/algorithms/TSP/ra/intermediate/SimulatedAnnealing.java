package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;

import java.util.Arrays;
import java.util.Random;


public class SimulatedAnnealing implements ILS {

	private int r = 100;
	private double alpha = 0.984018;

	private double START_TEMPERATURE = 100.0;
	private int seed;
	private Random random;

	public void setStartTemp(double temp) {
		this.START_TEMPERATURE = temp;
	}

	public double getStartTemp() {
		return START_TEMPERATURE;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getR() {
		return r;
	}

	public enum Mode {
		TwoOpt, ThreeOpt, DoubleBridge, RAND_CHOICE
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
		long target = System.currentTimeMillis() +
				1000 * 60 * 2 + 30 * 1000; // 2 min, 30 sec
		//double alpha = 1 - random.nextDouble() * 0.1 + 0.0001;

		System.out.println("Alpha is " + alpha + ", r=" + r);

		boolean end = false;

		while(temperature > 1E-9 && !end){
			for(int i=0; i<r; i++){
				SwappablePath next = null;

				switch(mode){
					case DoubleBridge:
						next = current.doubleBridge(
								getRandomOffsettedNumbers(4, length-1)
						);
						break;

					case TwoOpt:
						int[] ij_to = getRandomNumbers(2, length - 1);
						Arrays.sort(ij_to);
						next = current.twoOptSwap(ij_to[0], ij_to[1]);
						break;
					case RAND_CHOICE:
						if(random.nextBoolean()) {
							int[] ij = getRandomNumbers(2, length - 1);
							Arrays.sort(ij);
							next = current.twoOptSwap(ij[0], ij[1]);

							ij = getRandomNumbers(2, length - 1);
							Arrays.sort(ij);

							next = next.twoOptSwap(ij[0], ij[1]);
						} else {
							next = current.doubleBridge(
									getRandomOffsettedNumbers(4, length-1)
							);
						}
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
					end = true;
					break;
				}

				if(System.currentTimeMillis() >= target){
					end = true;
					break;
				}

				if(delta < 0){
					current = next;
					if(current.getLength() < best.getLength()){
						best = current;
					}
				}

				if(x < Math.exp(-delta * 1.0/temperature)){
					current = next;
				}
			}
			temperature *= alpha;


		}

		System.out.println("END! Alpha was " +
				"" + alpha + ", r=" + r + ", T = " + temperature);


		return new Route(best, data);

	}

	@Override
	public int getSeed() {
		return seed;
	}
}
