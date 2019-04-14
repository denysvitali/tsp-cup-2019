package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

public class City {
	private int id;

	public City(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof City){
			return ((City) obj).id == id;
		}

		return false;
	}
}
