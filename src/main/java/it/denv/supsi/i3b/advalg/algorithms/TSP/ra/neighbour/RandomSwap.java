package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.neighbour;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.NeighbourAlgorithm;

public class RandomSwap implements NeighbourAlgorithm {
	public int nswaps = 1;

	public RandomSwap(int nswaps){
		this.nswaps = nswaps;
	}

	@Override
	public Route computeNeighbour(Route r) {
		int[] path = r.getPath();
		assert(path.length > 1);

		for(int i=0; i<nswaps; i++){
			int a = 0;
			while(a == 0){
				a = (int) (Math.random() * path.length);
			}

			int b = a;

			while(a==b || b == path.length){
				b = (int) (Math.random() * path.length);
			}

			int v1 = path[a];
			path[a] = path[b];
			path[b] = v1;
		}

		return new Route(path, r.getData());
	}
}
