package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

public class ABEdge {
	private boolean isA = true;
	private int u = -1;
	private int v = -1;

	public ABEdge(int u, int v, boolean isA){
		this.u = u;
		this.v = v;
		this.isA = isA;
	}

	public int getU() {
		return u;
	}

	public int getV() {
		return v;
	}

	public boolean isA(){
		return isA;
	}
}
