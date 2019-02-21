package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public class Individual {
	private int[] genes;

	private TSPData tspData;
	private int fitness = -1;

	public Individual(TSPData tspData){
		this.tspData = tspData;
	}

	public Individual(Individual individual) {
		this.genes = new int[individual.getGenes().length];
		System.arraycopy(individual.getGenes(), 0, genes, 0,
				individual.getGenes().length);
		this.tspData = individual.tspData;
	}

	public int getFitness(){
		if(fitness != -1)
		{
			return fitness;
		}

		int startNode = tspData.getStartNode();
		int distance = 0;
		int[][] distMat = tspData.getDistances();


		for(int i=0; i<genes.length; i++){
			distance += distMat[startNode-1][genes[i]-1];
			startNode = genes[i];
		}

		distance += distMat[startNode-1][tspData.getStartNode()-1];

		fitness = distance;
		return distance;
	}


	public int[] getGenes() {
		return genes;
	}

	public void setGenes(int[] genes) {
		this.genes = genes;
	}

	@Override
	public String toString() {
		return String.format("Individual (Fit: %d)",
				getFitness());
	}
}
