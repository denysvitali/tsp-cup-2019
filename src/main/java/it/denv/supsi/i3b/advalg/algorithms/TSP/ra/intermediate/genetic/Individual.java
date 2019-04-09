package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

public class Individual {
	private boolean[] genes;
	private double fit;
	private Population from;

	public Individual(boolean[] genes, Population from){
		this.genes = genes;
		this.from = from;
	}

	int getFit(){
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
		return fit;
	}
}
