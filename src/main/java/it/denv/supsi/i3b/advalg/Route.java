package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;

import java.util.List;

public class Route {
	private List<Coordinate> coords;
	private int length;

	public Route(List<Coordinate> coords, int length){
		this.coords = coords;
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public List<Coordinate> getCoords() {
		return coords;
	}
}
