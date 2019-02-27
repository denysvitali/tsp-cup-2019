package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

public class Edge implements Comparable<Edge> {
	private int first;
	private int second;
	private int weight;

	public Edge(int first, int second, int weight){
		this.first = first;
		this.second = second;
		this.weight = weight;
	}

	public int getFirst() {
		return first;
	}

	public int getSecond() {
		return second;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Edge o) {
		if(this.weight < o.weight){
			return -1;
		} else if(this.weight > o.weight){
			return 1;
		}
		return 0;
	}
}
