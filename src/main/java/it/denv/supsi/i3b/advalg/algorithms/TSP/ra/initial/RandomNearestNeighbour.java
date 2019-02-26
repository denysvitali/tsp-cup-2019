package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;

public class RandomNearestNeighbour extends NearestNeighbour {

	@Override
	protected Edge getCandidate(){
		ArrayList<Edge> candidates = getCandidates();
		if(candidates.size() == 0){
			return null;
		}
		return candidates.get((int) ((Math.random() * 3) % candidates.size()));
	}
}
