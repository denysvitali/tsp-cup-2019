package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;

import java.util.*;

public class Ant {
	private AntColony colony;

	public TreeSet<Integer> unvisitedCities;
	public LinkedList<Integer> visitedCities;
	private int currentCity = -1;
	double[] probK;

	private AntStatus status = AntStatus.STOPPED;

	public Ant(AntColony antColony) {
		this.colony = antColony;
		this.unvisitedCities = new TreeSet<>();
		this.visitedCities = new LinkedList<>();

		for(int i=0; i<antColony.data.getDimension(); i++){
			unvisitedCities.add(i);
		}

		reset();
	}

	public void step() {
		if(status != AntStatus.STOPPED) {
			int nc = getNextCity();
			if(nc != -1){
				visit(nc);
			}
		}
	}

	public int getNextCity(){

		switch(colony.type){
			case ACS:
				double q = colony.random.nextDouble();

				if(q <= AntColonySystem.q0){
					Map<Integer, Double> jMap = new HashMap<>();
					double max = Double.MIN_VALUE;
					int maxEl = -1;

					if(unvisitedCities.size() == 0){
						setStopped();
						return visitedCities.get(0);
					}

					for(int l : unvisitedCities){
						double v = colony.getCurrentP(currentCity, l)
								* Math.pow(colony.heurN(currentCity, l),
								AntColonySystem.BETA);
						if(v > max){
							max = v;
							maxEl = l;
						}
					}

					System.out.println("MaxEl: " + maxEl + ", " + max);
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
		int first = getRandomCity();
		visit(first);
		status = AntStatus.RUNNING;
	}

	public void visit(int city) {
		this.unvisitedCities.remove(city);
		this.visitedCities.add(city);

		currentCity = city;
		probK = new double[colony.data.getDimension()];
		calculateProbs();
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
}
