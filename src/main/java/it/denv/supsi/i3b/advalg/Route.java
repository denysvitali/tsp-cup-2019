package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Route {
	private int[] path;
	private TSPData data;
	private int length = -1;

	int[][] incidenceMat = null;

	/*
		A Route is a complete Hamiltonian cycle,
		that is, given a starting node A,
		a Route can only start from A and end in A.

		E.g: [A, B, C, D, E, A]
	 */

	public Route(int[] path, TSPData data){
		this.path = path;
		this.data = data;
	}

	public Route(int[] path, int length, TSPData data){
		this.path = path;
		this.length = length;
		this.data = data;
	}

	public int getLength() {
		if(length == -1){
			calculateLength();
		}

		return length;
	}

	public int[] getPath() {
		return path;
	}

	private void calculateLength(){
		length = getSP().calulateDistance(data);
	}

	public void setPath(int[] new_path){
		path = new_path;
	}

	public SwappablePath getSP(){
		return new SwappablePath(getPath());
	}

	public int getStartNode() {
		return path[0];
	}

	public ArrayList<Coordinate> getCoords(){
		ArrayList<Coordinate> coords = new ArrayList<>(data.getCoordinates().values());
		return Arrays.stream(path)
				.mapToObj(coords::get)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public double compareTo(int bestKnown) {
		return this.length * 1.0 / bestKnown - 1;
	}

	public void setLength(int length){
		this.length = length;
	}

	public TSPData getData() {
		return data;
	}

	public boolean isValid() {
		Set<Integer> cities = new HashSet<>();

		for (int i=0; i<path.length-1; i++) {
			if(cities.contains(path[i])){
				return false;
			}
			cities.add(path[i]);
		}

		return path[0] == path[path.length-1];

	}

	private void genMatrix(){
		// Generate incidence matrix
		incidenceMat = new int[data.getDimension()][data.getDimension()];
		for(int i=1; i<path.length-1; i++){
			incidenceMat[i-1][i] = 1;
			incidenceMat[i][i-1] = 1;
		}
	}

	public boolean hasArc(int i, int j) {
		// GenMatrix
		if(incidenceMat == null){
			genMatrix();
		}

		return incidenceMat[i][j] == 1;
	}

	public int compare(Route r){
		int m_length = this.getLength();
		int o_length = r.getLength();

		if(m_length < o_length){
			return -1;
		} else if (m_length > o_length){
			return 1;
		}
		return 0;
	}
}
