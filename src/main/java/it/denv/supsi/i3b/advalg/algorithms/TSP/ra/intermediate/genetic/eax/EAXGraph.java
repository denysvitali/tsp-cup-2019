package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import java.util.ArrayList;

public class EAXGraph {
	private ArrayList<ABEdge> edges;

	public EAXGraph(){
		this.edges = new ArrayList<>();
	}

	public void addEdge(ABEdge a){
		this.edges.add(a);
	}
}
