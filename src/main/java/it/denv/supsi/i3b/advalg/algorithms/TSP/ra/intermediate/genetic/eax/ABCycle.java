package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

	public static List<ABCycle> getSubtours(List<ABEdge> edgesArray,
									  HashMap<Integer, ArrayList<ABEdge>> edges) {
		if(edges.size() == 0){
			return null;
		}

		List<ABCycle> cycles = new ArrayList<>();

		while(edgesArray.size() > 0){
			// Select the first city
			ArrayList<ABEdge> mEdges = new ArrayList<>();
			ABEdge firstEdge = edgesArray.get(0);

			int firstCity = firstEdge.getU();
			int currentCity = firstEdge.getV();
			mEdges.add(firstEdge);
			edges.get(firstEdge.getU()).remove(firstEdge);
			edges.get(firstEdge.getV()).remove(firstEdge);
			edgesArray.remove(firstEdge);



			while(currentCity != firstCity){
				ArrayList<ABEdge> edgesAtCity = edges.get(currentCity);

				if(edgesAtCity == null){
					throw new RuntimeException("No edge starts at"
							+ currentCity);
				}

				Optional<ABEdge> nextEdge =
						edgesAtCity.stream().findAny();

				if(nextEdge.isEmpty()){
					System.out.println(mEdges);
					throw new RuntimeException("Invalid E-Set!");
				}

				ABEdge nextEdgeV = nextEdge.get();

				if(nextEdgeV.getV() == currentCity){
					nextEdgeV = ABEdge.getInverted(nextEdgeV);
				}

				currentCity = nextEdgeV.getV();

				edges.get(nextEdgeV.getU()).remove(nextEdgeV);
				edges.get(nextEdgeV.getV()).remove(nextEdgeV);
				edgesArray.remove(nextEdgeV);
				mEdges.add(nextEdgeV);
			}
			cycles.add(new ABCycle(mEdges));
		}

		return cycles;
	}

	public List<ABCycle> intoSubtours() {
		HashMap<Integer, ArrayList<ABEdge>> nextNodes = new HashMap<>();

		for(ABEdge e : path){
			if(!nextNodes.containsKey(e.getU())){
				nextNodes.put(e.getU(), new ArrayList<>());
			}
			if(!nextNodes.containsKey(e.getV())){
				nextNodes.put(e.getV(), new ArrayList<>());
			}
			nextNodes.get(e.getU()).add(e);
			nextNodes.get(e.getV()).add(e);
		}

		return getSubtours(new ArrayList<>(path), nextNodes);
	}
}
