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
	String filePath = "";
	protected int dimension;
	EdgeWeightType ewt;
	protected int best_known = 0;
	HashMap<Integer, Coordinate> coordinates = new HashMap<>();
	private HashMap<Integer, int[]> nearest = new HashMap<>();
	protected ArrayList<Edge> edges = new ArrayList<>();
	private boolean init = false;


	private int[][] distances;
	private int startNode = -1;

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

	public int getDim() {
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

	public int[] getCL(int startNode) {
		return nearest.get(startNode);
	}

	public void setCL(HashMap<Integer, int[]> nearest) {
		this.nearest = nearest;
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public boolean getInit() {
		return this.init;
	}

	public String getFilePath() {
		return this.filePath;
	}
}
