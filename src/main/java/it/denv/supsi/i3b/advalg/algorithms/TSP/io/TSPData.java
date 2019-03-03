package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.EdgeWeightType;
import it.denv.supsi.i3b.advalg.algorithms.ProblemType;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class TSPData {
	protected String name;
	protected ProblemType type = ProblemType.TSP;
	protected String comment = "";
	protected int dimension;
	protected EdgeWeightType ewt;
	protected int best_known = 0;
	protected HashMap<Integer, Coordinate> coordinates = new HashMap<>();
	protected HashMap<Integer, TreeSet<Edge<Integer>>> nearest = new HashMap<>();
	protected ArrayList<Edge<Integer>> edges = new ArrayList<>();


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

	public HashMap<Integer, Coordinate> getCoordinates() {
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

	public TreeSet<Edge<Integer>> getNearest(int startNode) {
		return nearest.get(startNode);
	}

	public void setNearest(HashMap<Integer, TreeSet<Edge<Integer>>> nearest) {
		this.nearest = nearest;
	}

	public ArrayList<Edge<Integer>> getEdges() {
		return this.edges;
	}

	public void setEdges(ArrayList<Edge<Integer>> edges) {
		this.edges = edges;
	}
}
