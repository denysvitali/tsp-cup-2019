package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant {
	protected AntColony colony;

	private boolean[] visitedCities;
	public int[] path;

	private int visitedCount = 0;
	private int currentCity = -3;
	private int firstCity = -2;
	private int id = -1;

	private List<City> notVisited = new ArrayList<>();

	private AntStatus status = AntStatus.STOPPED;

	public Ant(AntColony antColony) {
		this.colony = antColony;
		reset();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void step() {
		if (status == AntStatus.STOPPED) {
			return;
		}

		printStatus("Step()");
		if (status != AntStatus.STOPPED) {
			int nc = getNextCity();
			printStatus("Next City is " + nc);
			visit(nc);
		}
	}

	private void printStatus(String s) {
		if (AntColony.DEBUG) {
			System.out.println(this + ": " + s);
		}
	}

	@Override
	public String toString() {
		return "Ant#" + id + " (FC: " +
				firstCity + "," +
				" VC: " + visitedCount + ")";
	}

	public int getNextCity() {

		switch (colony.type) {
			case ACS:

				if (visitedCount == colony.data.getDim()) {
					return firstCity;
				}

				double q = colony.random.nextDouble();

				// Pseudorandom Proportional Rule
				if (q <= colony.getParams().getQ0()) {
					// Choose the node w/ the highest Pheromone * Heur
					return getBestNext(currentCity);
				} else {
					// J
					/* J = random variable given by (4.1) with α = 1 */

					int i = currentCity;

					if (colony.USE_CL) {
						double sum_prob = 0;
						int[] cl = colony.data.getCL(currentCity);
						double[] prob = new double[cl.length];
						for (int l = 0; l < cl.length; l++) {
							if (!visitedCities[cl[l]]) {
								prob[l] += colony.getChoiceInfo(i, l);
								sum_prob += prob[l];
							}
						}

						if (sum_prob == 0.0) {
							return getBestNext(i);
						}

						int nextCity = getRandomCityByProb(colony.random, prob, sum_prob);
						return cl[nextCity];
					} else {
						double[] prob = new double[notVisited.size()];
						double sum_prob = 0;
						for (int j = 0; j < notVisited.size(); j++) {
							prob[j] = colony.getChoiceInfo(i, j);
							sum_prob += prob[j];
						}

						int nextCity = getRandomCityByProb(colony.random, prob, sum_prob);

						if (nextCity != -1) {
							return notVisited.get(nextCity).getId();
						}

						return getNextUnvisitedCity();
					}

				}
		}
		return -1;
	}

	public int getBestNext(int currentCity) {
		double v = 0.0;
		int nc = -1;
		for (int j = 0; j < colony.data.getDim(); j++) {
			if (!visitedCities[j]) {
				if (colony.getChoiceInfo(currentCity, j) > v) {
					v = colony.getChoiceInfo(currentCity, j);
					nc = j;
				}
			}
		}
		return nc;
	}

	public static int getRandomCityByProb(Random random, double[] prob, double sumProb) {
		double r = random.nextDouble() * sumProb;
		int j = 0;
		double p = prob[j];

		while (p < r) {
			j++;
			p += prob[j];
		}
		return j;

		//throw new RuntimeException("This should never happen");
	}

	public static int getRandomCityByProb(Random r, double[] prob) {
		double rand = r.nextDouble();
		for (int i = 0; i < prob.length; i++) {
			rand -= prob[i];
			if (0 >= rand) {
				return i;
			}
		}

		return -1;

		//throw new RuntimeException("This should never happen");
	}

	private void setStopped() {
		status = AntStatus.STOPPED;
	}

	public AntStatus getStatus() {
		return status;
	}

	public void reset() {
		this.visitedCount = 0;
		visitedCities = new boolean[colony.data.getDim()];
		notVisited = new ArrayList<>();

		for (int i = 0; i < colony.data.getDim(); i++) {
			visitedCities[i] = false;
			notVisited.add(new City(i));
		}

		this.path = new int[colony.data.getDim() + 1];

		firstCity = getRandomCity();
		currentCity = -1;
		visit(firstCity);
		status = AntStatus.RUNNING;
	}

	public void visit(int city) {

		assert (city != -1);

		if (city == firstCity && currentCity != -1) {
			currentCity = city;
			this.path[visitedCount] = city;
			visitedCount++;
			setStopped();
			printStatus("Stopping because " + city + " has been reached");
			return;
		}

		if (currentCity != -1) {
			colony.localUpdate(currentCity, city);
		}

		this.visitedCities[city] = true;
		this.notVisited.remove(new City(city));
		this.path[visitedCount] = city;

		currentCity = city;
		visitedCount++;
	}

	private int getNextUnvisitedCity() {
		if (notVisited.size() > 0) {
			int nextCity = notVisited.get(0).getId();
			return nextCity;
		}

		return -1;
	}

	private int getRandomCity() {
		return colony.random.nextInt(colony.data.getDim());
	}

	public Route getRoute() {
		path[colony.data.getDim()] = firstCity;

		return new Route(path, colony.data);
	}

	public int getVisited() {
		return visitedCount;
	}
}
