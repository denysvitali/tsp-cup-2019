package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.LinkedList;

public class SwappablePath {
	private int[] path;

	public SwappablePath(int[] path){
		this.path = path;
	}

	public SwappablePath twoOptSwap(int i, int k){
		/*
		2optSwap(route, i, k) {
			1. take route[0] to route[i-1] and add them in order to new_route
			2. take route[i] to route[k] and add them in reverse order to new_route
			3. take route[k+1] to end and add them in order to new_route
			return new_route;
		}
		*/

		assert(i != 0);
		assert(k != path.length - 1);

		int[] np = new int[this.path.length];

		for(int j=0; j<i; j++){
			np[j] = path[j];
		}

		for(int j=i; j<=k; j++){
			np[k-j + i] = path[j];
		}

		for(int j=k+1; j<this.path.length; j++){
			np[j] = path[j];
		}

		return new SwappablePath(np);
	}

	public LinkedList<Integer> getPath(){
		LinkedList<Integer> ll = new LinkedList<>();
		for (int value : path) {
			ll.add(value);
		}

		return ll;
	}
	public int[] getPathArr(){
		return path;
	}

	public int calulateDistance(TSPData data) {
		int[][] distances = data.getDistances();

		int distance = 0;
		for(int i = 0; i+1<path.length; i++){
			int a = path[i];
			int b = path[i+1];

			distance += distances[a][b];
		}
		return distance;
	}
}
