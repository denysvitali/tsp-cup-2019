package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;

public class Ant {
	private AntColony colony;

	private boolean[] visitedCities;
	public int[] path;

	private int visitedCount = 0;
	private double[] probK;
	private int currentCity = -3;
	private int firstCity = -2;
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
				firstCity + "," +
				" VC: " + visitedCount + ")";
	}

	public int getNextCity(){

		switch(colony.type){
			case ACS:

				if(visitedCount == colony.data.getDimension()){
					return firstCity;
				}

				double q = colony.random.nextDouble();

				// Pseudorandom Proportional Rule
				if(q <= AntColonySystem.q0){
					double max = Double.MIN_VALUE;
					int maxEl = firstCity;

					for(int l = 0; l<colony.data.getDimension(); l++){
						if(!visitedCities[l]) {
							double v = colony.getCurrentP(currentCity, l)
									* Math.pow(colony.heurN(currentCity, l),
									AntColonySystem.BETA);
							if (v > max) {
								max = v;
								maxEl = l;
							}
						}
					}

					printStatus("MaxEl: " + maxEl + ", " + max);
					return maxEl;
				} else {
					// J
					/* J = random variable given by (4.1) with α = 1 */

					double rand = colony.random.nextDouble();
					for(int i=0; i < probK.length; i++){
						rand -= probK[i];

						if(rand <= 0){
							return i;
						}
					}

					return getNextUnvisitedCity();
				}
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
		this.visitedCount = 0;
		visitedCities = new boolean[colony.data.getDimension()];

		for(int i=0; i<colony.data.getDimension(); i++){
			visitedCities[i] = false;
		}

		this.path = new int[colony.data.getDimension() + 1];

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
		this.path[visitedCount] = city;

		currentCity = city;
		probK = new double[colony.data.getDimension()];
		calculateProbs();
		//printStatus("I'm at city " + city);
		visitedCount++;

		// Local Update
	}

	private void calculateProbs() {
		switch (colony.type){
			case ACS:
				int r = currentCity;

				double sum = 0.0;
				int[] cl = colony.data.getCL(currentCity);
				double[] num = new double[cl.length];

				if (cl.length == 0){
					probK = new double[probK.length];
					return;
				}

				for(int s=0; s<cl.length; s++){
					if(!this.visitedCities[cl[s]]) {
						num[s] = Math.pow(
								colony.getCurrentP(r, cl[s]),
								AntColonySystem.ALPHA);
						num[s] *= Math.pow(
								colony.heurN(r, cl[s]),
								AntColonySystem.BETA
						);
						sum += num[s];
					}
				}

				for(int s = 0; s<cl.length; s++){
					if(!this.visitedCities[cl[s]]) {
						int second = cl[s];
						probK[second] = num[s] / sum;
					} else {
						probK[cl[s]] = 0.0;
					}
				}

				break;
		}
	}

	private Edge<Integer> randomUnvisitedEdge(int u){
		int v = getNextUnvisitedCity();
		return new Edge<>(u, v, colony.data.getDistances()[u][v]);
	}

	private int getNextUnvisitedCity(){
		for(int i=0; i<visitedCities.length; i++){
			if(!visitedCities[i]){
				return i;
			}
		}
		return -1;
	}

	private int getRandomCity() {
		return colony.random.nextInt(colony.data.getDimension());
	}

	public Route getRoute() {
		path[colony.data.getDimension()] = firstCity;

		return new Route(path, colony.data);
	}

	public int getVisited() {
		return visitedCount;
	}
}
