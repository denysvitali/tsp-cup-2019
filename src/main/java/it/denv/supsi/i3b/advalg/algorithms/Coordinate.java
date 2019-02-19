package it.denv.supsi.i3b.advalg.algorithms;

public class Coordinate {
	private int number;
	private double x;
	private double y;

	public Coordinate(int number, double x, double y){
		this.number = number;
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
