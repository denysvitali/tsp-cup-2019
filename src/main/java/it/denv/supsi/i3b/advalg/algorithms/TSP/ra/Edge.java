package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import java.util.Comparator;

public class Edge implements Comparable<Edge> {
	private int first;
	private int second;
	private int weight;

	public Edge(int first, int second, int weight){
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

	public int getWeight() {
		return weight;
	}

	public Edge invert() {
		return new Edge(second, first, weight);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Edge)){
			return false;
		}

		Edge o = (Edge) obj;

		if(first == o.first){
			if(second == o.second){
				return weight == o.weight;
			}
			return false;
		} else {
			if(first == o.second){
				if(second == o.first){
					// Same but inverted :)
					return weight == o.weight;
				}
			}
		}

		return false;
	}

	@Override
	public int compareTo(Edge o) {
		int compareRes = Integer.compare(this.weight, o.weight);

		if(compareRes == 0){
			if(this.equals(o)){
				return 0;
			}

			return -1;
		}

		return compareRes;
	}
}
