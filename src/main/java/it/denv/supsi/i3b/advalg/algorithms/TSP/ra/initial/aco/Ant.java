package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ant {
	private AntColony colony;

	public TreeSet<Integer> unvisitedCities;
	public LinkedList<Integer> visitedCities;
	double[] probK;
	private int currentCity = -1;
	private int firstCity = -1;
	private int id = -1;

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
				firstCity + ", UC: " + unvisitedCities.size() +
				" VC: " + visitedCities.size() + ")";
	}

	public int getNextCity(){

		switch(colony.type){
			case ACS:

				if(unvisitedCities.size() == 0){
					return firstCity;
				}

				double q = colony.random.nextDouble();

				// Pseudorandom Proportional Rule
				if(q <= AntColonySystem.q0){
					double max = Double.MIN_VALUE;
					int maxEl = unvisitedCities.first();

					for(int l : unvisitedCities){
						double v = colony.getCurrentP(currentCity, l)
								* Math.pow(colony.heurN(currentCity, l),
								AntColonySystem.BETA);
						if(v > max){
							max = v;
							maxEl = l;
						}
					}

					printStatus("MaxEl: " + maxEl + ", " + max);
					return maxEl;
				} else {
					// J
					/* J = random variable given by (4.1) with alpha = 1 */
					double rand = colony.random.nextDouble();
					double tmp_sum = 0;
					for(int i=0; i < probK.length; i++){
						tmp_sum += probK[i];

						if(tmp_sum >= rand){
							return i;
						}
					}
				}
				break;
		}

		return -1;
	}

	private void setStopped() {
		status = AntStatus.STOPPED;
	}

	public AntStatus getStatus() {
		return status;
	}

	public void reset() {
		this.unvisitedCities = new TreeSet<>();
		this.visitedCities = new LinkedList<>();

		for(int i=0; i<colony.data.getDimension(); i++){
			unvisitedCities.add(i);
		}

		firstCity = getRandomCity();
		currentCity = -1;
		visit(firstCity);
		status = AntStatus.RUNNING;
	}

	public void visit(int city) {

		if(city == firstCity && currentCity != -1){
			currentCity = city;
			setStopped();
			printStatus("Stopping because " + city + " has been reached");
			return;
		}

		if(currentCity != -1){
			colony.localUpdate(currentCity, city);
		}

		this.unvisitedCities.remove(city);
		this.visitedCities.add(city);

		currentCity = city;
		probK = new double[colony.data.getDimension()];
		calculateProbs();
		printStatus("I'm at city " + city);

		// Local Update
	}

	private void calculateProbs() {
		switch (colony.type){
			case ACS:
				double[] num = new double[colony.data.getDimension()];
				int r = visitedCities.getLast();

				double sum = 0.0;

				for(Integer s : unvisitedCities){
					num[s] = colony.getCurrentP(r, s);
					sum += num[s];
				}

				for(Integer s : unvisitedCities){
					probK[s] = num[s] / sum;
				}

				break;
		}
	}

	private int getRandomCity() {
		return colony.random.nextInt(colony.data.getDimension());
	}

	public List<Integer> getTabuList() {
		return visitedCities;
	}

	private double getProbability(int j){
		// Given the current node (i), what's the prob. of visiting j?
		return probK[j];
	}

	public Route getRoute() {
		int[] path = new int[colony.data.getDimension() + 1];
		for(int i=0; i<colony.data.getDimension(); i++){
				path[i] = visitedCities.get(i);
		}

		path[colony.data.getDimension()] = firstCity;

		return new Route(path, colony.data);
	}
}
