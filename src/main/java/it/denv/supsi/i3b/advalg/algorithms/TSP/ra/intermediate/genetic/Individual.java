package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

import java.util.Arrays;

public class Individual {
	private boolean[] genes;
	private int fit = -1;
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

	public String prettyPrintGenes() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<genes.length; i++){
			if(genes[i]){
				sb.append("1");
			} else {
				sb.append("0");
			}
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}

		if(obj instanceof Individual){
			Individual o = (Individual) obj;
			return Arrays.equals(this.genes, o.genes);
		}
		return false;
	}
}
