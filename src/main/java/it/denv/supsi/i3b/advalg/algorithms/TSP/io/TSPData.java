package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.EdgeWeightType;
import it.denv.supsi.i3b.advalg.algorithms.ProblemType;

import java.util.ArrayList;

public class TSPData {
	protected String name;
	protected ProblemType type = ProblemType.TSP;
	protected String comment = "";
	protected int dimension;
	protected EdgeWeightType ewt;
	protected int best_known = 0;
	protected ArrayList<Coordinate> coordinates = new ArrayList<>();
	protected double[][] distances;

	protected TSPData(){

	}

	public String getName() {
		return name;
	}

	public ProblemType getType() {
		return type;
	}

	public String getComment() {
		return comment;
	}

	public int getDimension() {
		return dimension;
	}

	public EdgeWeightType getEwt() {
		return ewt;
	}

	public int getBestKnown() {
		return best_known;
	}

	public ArrayList<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setDistances(double[][] distances) {
		this.distances = distances;
	}

	public double[][] getDistances() {
		return distances;
	}
}
