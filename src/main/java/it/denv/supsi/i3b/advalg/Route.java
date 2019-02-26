package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Route {
	private LinkedList<Coordinate> coords;
	private int length;
	private int startNode;

	public Route(int startNode, LinkedList<Coordinate> coords, int length){
		this.startNode = startNode;
		this.coords = coords;
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public LinkedList<Coordinate> getCoords() {
		return coords;
	}

	public int[] getPath() {
		return coords.stream().mapToInt(Coordinate::getNumber).toArray();
	}

	public void setPath(int[] new_path){
		LinkedList<Coordinate> coordinates = new LinkedList<>();
		LinkedList<Coordinate> sorted_coords = coords.stream()
				.sorted(Comparator.comparing(Coordinate::getNumber))
				.collect(Collectors.toCollection(LinkedList::new));

		for (int value : new_path) {
			coordinates.add(sorted_coords.get(value - 1));
		}

		coords = coordinates;
	}

	public SwappablePath getSP(){
		return new SwappablePath(getPath());
	}

	public int getStartNode() {
		return startNode;
	}

	public double compareTo(int bestKnown) {
		return this.length * 1.0 / bestKnown - 1;
	}

	public void setLength(int length){
		this.length = length;
	}
}
