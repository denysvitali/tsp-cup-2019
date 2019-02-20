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
	protected int[][] distances;
	protected int startNode = -1;

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

	public void setDistances(int[][] distances) {
		this.distances = distances;
	}

	public int[][] getDistances() {
		return distances;
	}

	public void setStartNode(int startNode) {
		this.startNode = startNode;
	}

	public int getStartNode() {
		return startNode;
	}
}
