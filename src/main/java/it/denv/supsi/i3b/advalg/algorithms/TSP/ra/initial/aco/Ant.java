package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant {
	private AntColony colony;

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

	public void setId(int id){
		this.id = id;
	}

	public void step() {
		if(status == AntStatus.STOPPED){
			return;
		}

		printStatus("Step()");
		if(status != AntStatus.STOPPED) {
			int nc = getNextCity();
			printStatus("Next City is " + nc);
			visit(nc);
		}
	}

	private void printStatus(String s) {
		if(AntColony.DEBUG){
			System.out.println(this + ": " + s);
		}
	}

	@Override
	public String toString(){
		return "Ant#" + id + " (FC: " +
				firstCity + "," +
				" VC: " + visitedCount + ")";
	}

	public int getNextCity(){

		switch(colony.type){
			case ACS:

				if(visitedCount == colony.data.getDim()){
					return firstCity;
				}

				double q = colony.random.nextDouble();

				// Pseudorandom Proportional Rule
				if(q <= colony.q0){
					// Choose the node w/ the highest Pheromone * Heur
					double max = Double.MIN_VALUE;

					int maxEl = firstCity;
					for(City c : notVisited){
						int l = c.getId();
						double v = colony.getCurrentP(currentCity, l)
								* Math.pow(colony.heurN(currentCity, l),
								colony.BETA);
						if (v > max) {
							max = v;
							maxEl = l;
						}
					}
					return maxEl;
				} else {
					// J
					/* J = random variable given by (4.1) with α = 1 */

					int i = currentCity;

					if (colony.USE_CL) {
						double den = 0;
						int[] cl = colony.data.getCL(currentCity);
						double[] prob = new double[cl.length];
						for (int l = 0; l < cl.length; l++) {
							if (!visitedCities[cl[l]]){
								den +=
										Math.pow(colony.getCurrentP(i, cl[l]),
												colony.ALPHA)
												* Math.pow(colony.heurN(i, cl[l]),
												colony.BETA);
							}
						}

						for (int j = 0; j < cl.length; j++) {
							if (!visitedCities[cl[j]]){
								prob[j] = Math.pow(colony.getCurrentP(i, cl[j]),
										colony.ALPHA)
										* Math.pow(colony.heurN(i, cl[j]),
										colony.BETA) / den;
							} else {
								prob[j] = 0.0;
							}
						}

						int nextCity = getRandomCityByProb(colony.random, prob);
						if (nextCity != -1) {
							return cl[nextCity];
						}
						return getNextUnvisitedCity();
					} else {
						double[] prob = new double[notVisited.size()];
						double den = 0;
						for (City c : notVisited) {
							int l = c.getId();
							den +=
									Math.pow(colony.getCurrentP(i, l),
											colony.ALPHA)
											* Math.pow(colony.heurN(i, l),
											colony.BETA);
						}

						for (int j=0; j<notVisited.size(); j++) {
							prob[j] = Math.pow(colony.getCurrentP(i, notVisited.get(j).getId()),
									colony.ALPHA)
									* Math.pow(colony.heurN(i, notVisited.get(j).getId()),
									colony.BETA) / den;
						}

						int nextCity = getRandomCityByProb(colony.random, prob);

						if(nextCity != -1){
							return notVisited.get(nextCity).getId();
						}

						return getNextUnvisitedCity();
					}

					}
				}
				return -1;
	}

	public static int getRandomCityByProb(Random r, double[] prob) {
		double rand = r.nextDouble();
		for(int i=0; i<prob.length; i++){
			rand -= prob[i];
			if(0 >= rand){
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

		for(int i = 0; i<colony.data.getDim(); i++){
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

		assert(city != -1);

		if(city == firstCity && currentCity != -1){
			currentCity = city;
			this.path[visitedCount] = city;
			visitedCount++;
			setStopped();
			printStatus("Stopping because " + city + " has been reached");
			return;
		}

		if(currentCity != -1){
			colony.localUpdate(currentCity, city);
		}

		this.visitedCities[city] = true;
		this.notVisited.remove(new City(city));
		this.path[visitedCount] = city;

		currentCity = city;
		visitedCount++;
	}

	private int getNextUnvisitedCity(){
		if(notVisited.size() > 0){
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
