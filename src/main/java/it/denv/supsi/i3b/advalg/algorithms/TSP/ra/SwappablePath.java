package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.Route;
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
	public int[] getPathArr(){
		return this.path.stream().mapToInt(Integer::intValue).toArray();
	}

	public int calulateDistance(TSPData data) {
		int[][] distances = data.getDistances();

		int distance = 0;
		for(int i = 0; i+1<path.size(); i++){
			int a = path.get(i);
			int b = path.get(i+1);

			distance += distances[a][b];
		}
		return distance;
	}

	public Route toRoute(TSPData data) {
		Route newRoute = new Route(getPathArr(), data);
		newRoute.setLength(this.calulateDistance(data));

		return newRoute;
	}
}
