package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

public class Edge {
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

	public static int compare(Edge o1, Edge o2) {
		if(o1.weight < o2.weight){
			return -1;
		} else if(o1.weight > o2.weight){
			return 1;
		}
		return 0;
	}
}
