package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import java.util.ArrayList;
import java.util.List;

public class ABCycle {
	private List<ABEdge> path;

	public ABCycle(){
		path = new ArrayList<>();
	}

	public ABCycle(List<ABEdge> edgeList){
		this.path = edgeList;
	}

	public List<ABEdge> getPath() {
		return path;
	}

	public void add(ABEdge e){
		path.add(e);
	}

	public boolean contains(ABEdge e){
		return path.contains(e);
	}
}
