package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public class Individual {
	private int[] genes;

	private TSPData tspData;

	public Individual(TSPData tspData){
		this.tspData = tspData;
	}

	public double getFitness(){
		return 0;
	}

	public int[] getGenes() {
		return genes;
	}

	public void setGenes(int[] genes) {
		this.genes = genes;
	}
}
