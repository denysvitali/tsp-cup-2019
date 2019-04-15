package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import java.util.ArrayList;
import java.util.HashMap;

public class EAXGraph {
	private ArrayList<ABEdge> edges;
	private HashMap<Integer, ArrayList<ABEdge>> cities;

	public EAXGraph(){
		this.edges = new ArrayList<>();
		this.cities = new HashMap<>();
	}

	public void addEdge(ABEdge a){
		this.edges.add(a);

		if(!this.cities.containsKey(a.getU())){
			this.cities.put(a.getU(), new ArrayList<>());
		}

		if(!this.cities.containsKey(a.getV())) {
			this.cities.put(a.getV(), new ArrayList<>());
		}

		this.cities.get(a.getU()).add(a);
		this.cities.get(a.getV()).add(a);
	}

	public ArrayList<ABEdge> getEdges() {
		return edges;
	}

	public HashMap<Integer, ArrayList<ABEdge>> getCities() {
		return cities;
	}

	public ArrayList<ABEdge> getCity(int idx){
		return this.cities.get(idx);
	}

	public void removeEdge(ABEdge e) {
		this.edges.remove(e);
	}
}
