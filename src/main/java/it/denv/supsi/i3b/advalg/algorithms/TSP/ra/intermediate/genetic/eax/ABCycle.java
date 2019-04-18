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

	public void addAll(List<ABEdge> l){
		this.path.addAll(l);
	}

	public static ABCycle getCycle(List<ABEdge> edges) {
		for(int i=0; i<edges.size(); i++) {
			int startCity = edges.get(i).getU();

			for(int j=i; j<edges.size(); j++){
				if(edges.get(j).getV() == startCity){
					return new ABCycle(edges.subList(i,j+1));
				}
			}
		}

		return null;
	}

	public static ABCycle getCycleArr(List<ABEdge> edges) {
		if(edges.size() == 0){
			return null;
		}

		ArrayList<ABEdge> mEdges = new ArrayList<>(edges);

		ABCycle c = new ABCycle();
		c.add(edges.get(0));
		int currentCity = edges.get(0).getV();
		int firstCity = edges.get(0).getU();

		for(int i=1; i<mEdges.size(); i++){
			ABEdge currentEdge = edges.get(i);

			if(currentEdge.getU() == currentCity){
				c.add(edges.get(i));
				currentCity = edges.get(i).getV();
				mEdges.remove(edges.get(i));
				i=1;

				if(currentCity == firstCity){
					return c;
				}
			}
		}

		return null;
	}

	public ArrayList<ABCycle> intoSubtours() {
		ArrayList<ABEdge> copyEdges = new ArrayList<>(path);
		ArrayList<ABCycle> cycles = new ArrayList<>();

		ABCycle currentC;
		while((currentC = getCycleArr(copyEdges)) != null){
			cycles.add(currentC);
			copyEdges.removeAll(currentC.getPath());
		}
		return cycles;
	}
}
