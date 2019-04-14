package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

public class Individual {
	private boolean[] genes;
	private int fit;
	private Population from;

	public Individual(boolean[] genes, Population from){
		this.genes = genes;
		this.from = from;
	}

	public int getFit(){
		if(this.fit != -1){
			return this.fit;
		}

		int[][] im = from.getIncidenceMatrix();
		int fit = 0;

		int idx = 0;
		for(int i=0; i<from.getDimension(); i++){
			for(int j=i+1; j<from.getDimension(); j++){
				if(genes[idx]){
					fit += im[i][j];
				}
				idx++;
			}
		}
		this.fit = fit;
		return fit;
	}

	public boolean[] getGenes(){
		return genes;
	}
}
