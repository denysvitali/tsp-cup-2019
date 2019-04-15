package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import java.util.ArrayList;

public class ABCycle {
	private ArrayList<ABEdge> path;

	public ABCycle(){
		path = new ArrayList<>();
	}

	public ArrayList<ABEdge> getPath() {
		return path;
	}

	public void add(ABEdge e){
		path.add(e);
	}

	public boolean contains(ABEdge e){
		return path.contains(e);
	}
}
