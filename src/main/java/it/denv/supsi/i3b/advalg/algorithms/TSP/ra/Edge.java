package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

public class Edge<T extends Comparable<T>> implements Comparable<Edge<T>> {
	private int first;
	private int second;
	private T weight;

	public Edge(int first, int second, T weight){
		this.first = first;
		this.second = second;
		this.weight = weight;
	}

	public int getU() {
		return first;
	}

	public int getV() {
		return second;
	}

	public T getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Edge<T> o) {
		return this.weight.compareTo(o.weight);
	}

	public Edge<T> invert() {
		return new Edge<>(second, first, weight);
	}
}
