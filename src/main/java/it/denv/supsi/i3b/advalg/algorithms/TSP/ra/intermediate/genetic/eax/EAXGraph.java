package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EAXGraph {
	private ArrayList<ABEdge> edges;
	private HashMap<Integer, ArrayList<ABEdge>> cities;

	private ArrayList<ABEdge> aList;
	private ArrayList<ABEdge> bList;

	public EAXGraph(){
		this.edges = new ArrayList<>();
		this.cities = new HashMap<>();

		this.aList = new ArrayList<>();
		this.bList = new ArrayList<>();
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

		if(a.isA()){
			this.aList.add(a);
		} else {
			this.bList.add(a);
		}
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

	public EAXGraph clone(){
		EAXGraph g = new EAXGraph();
		g.edges = (ArrayList<ABEdge>) this.edges.clone();
		g.cities = (HashMap<Integer, ArrayList<ABEdge>>) this.cities.clone();
		g.aList = (ArrayList<ABEdge>) this.aList.clone();
		g.bList = (ArrayList<ABEdge>) this.bList.clone();

		return g;
	}

	public void removeFromA(ABEdge e) {
		this.aList.remove(e);
	}

	public void removeFromB(ABEdge e) {
		this.bList.remove(e);
	}


	public ArrayList<ABEdge> getAList() {
		return aList;
	}

	public ArrayList<ABEdge> getBList(){
		return bList;
	}
}
