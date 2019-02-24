package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;

import java.util.LinkedList;

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

	public int getStartNode() {
		return startNode;
	}
}
