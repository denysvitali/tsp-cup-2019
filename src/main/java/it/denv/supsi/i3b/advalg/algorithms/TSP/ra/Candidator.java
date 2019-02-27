package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import java.util.ArrayList;

public interface Candidator {
	void computeCandidates();
	ArrayList<Edge> getCandidates(int startNode);
}
