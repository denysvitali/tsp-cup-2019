package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import java.util.ArrayList;

public interface Candidator<T extends Comparable<T>> {
	void computeCandidates();
	ArrayList<Edge<T>> getCandidates(int startNode);
}
