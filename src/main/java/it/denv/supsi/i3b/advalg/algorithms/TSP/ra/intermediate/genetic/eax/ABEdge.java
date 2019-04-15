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

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}

		if(obj instanceof ABEdge){
			ABEdge o = (ABEdge) obj;
			if(this.isA != o.isA){
				return false;
			}

			if(this.u == o.u && this.v == o.v){
				return true;
			}

			// Asymmetric TSP:
			if(this.v == o.u && this.u == o.v){
				return true;
			}
		}

		return false;
	}
}
