package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidate;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class NearestNeighborCandidator implements Candidator {

	private int size;
	private TSPData data;

	ArrayList<Edge> candidateMap = new ArrayList<>();

	public NearestNeighborCandidator(int size, TSPData data){
		this.size = size;
		this.data = data;
	}

	@Override
	public void computeCandidates() {
		// NO-OP
	}

	@Override
	public ArrayList<Edge> getCandidates(int startNode) {
		return
				data.getNearest(startNode)
						.stream()
						.limit(size)
						.collect(Collectors.toCollection(ArrayList::new));
	}
}
