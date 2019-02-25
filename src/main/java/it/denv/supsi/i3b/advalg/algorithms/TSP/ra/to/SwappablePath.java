package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.Collections;
import java.util.LinkedList;

public class SwappablePath {
	private LinkedList<Integer> path = new LinkedList<>();

	public SwappablePath(int[] path){
		for(int i = 0; i< path.length; i++){
			this.path.add(i, path[i]);
		}
	}

	public SwappablePath(LinkedList<Integer> path){
		this.path = path;
	}

	public SwappablePath swap(int i, int k){
		assert(i != 0);
		assert(k != path.size());

		LinkedList<Integer> newRoute = new LinkedList<>();
		for(int j=0; j<i; j++){
			newRoute.add(path.get(j));
		}

		LinkedList<Integer> reverseMe = new LinkedList<>();
		for(int j=i; j<=k; j++){
			reverseMe.add(path.get(j));
		}
		Collections.reverse(reverseMe);
		newRoute.addAll(reverseMe);

		for(int j=k+1; j<path.size(); j++){
			newRoute.add(path.get(j));
		}

		return new SwappablePath(newRoute);
	}

	public LinkedList<Integer> getPath(){
		return this.path;
	}

	public int calulateDistance(TSPData data) {
		int[][] distances = data.getDistances();

		int distance = 0;
		for(int i = 0; i+1<path.size(); i++){
			int a = path.get(i) - 1;
			int b = path.get(i+1) - 1;

			distance += distances[a][b];
		}
		return distance;
	}
}
