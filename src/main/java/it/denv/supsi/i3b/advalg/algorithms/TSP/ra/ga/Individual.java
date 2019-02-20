package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public class Individual {
	private int[] genes;

	private TSPData tspData;

	public Individual(TSPData tspData){
		this.tspData = tspData;
	}

	public int getFitness(){
		int startNode = tspData.getStartNode();
		int distance = 0;
		int[][] distMat = tspData.getDistances();


		for(int i=0; i<genes.length; i++){
			distance += distMat[startNode-1][genes[i]-1];
			startNode = genes[i];
		}

		distance += distMat[startNode-1][tspData.getStartNode()-1];

		return distance;
	}

	public int[] getGenes() {
		return genes;
	}

	public void setGenes(int[] genes) {
		this.genes = genes;
	}
}
