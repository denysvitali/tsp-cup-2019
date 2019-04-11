package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public class SwappablePath {
	private int[] path;
	private int length = -1;

	public SwappablePath(int[] path){
		this.path = path;
	}

	public SwappablePath twoOptSwap(int p, int q){
		/*
		2optSwap(route, i, k) {
			1. take route[0] to route[i-1] and add them in order to new_route
			2. take route[i] to route[k] and add them in reverse order to new_route
			3. take route[k+1] to end and add them in order to new_route
			return new_route;
		}
		*/

		int[] np = new int[this.path.length];

		for(int i=0; i<p; i++){
			np[i] = path[i];
		}

		int dec = 0;
		for(int i=p; i<=q; i++){
			np[i] = path[(q - dec)];
			dec++;
		}

		for(int i=q+1; i<this.path.length; i++){
			np[i] = path[i];
		}


		return new SwappablePath(np);
	}

	public SwappablePath[] threeOptSwap(int i, int j, int k){
		assert(i+1 != j);
		assert(j+1 != k);
		assert(k+1 != i);

		assert(i < j);
		assert(j < k);

		// First Move

		// i - (i+1) becomes i - (j+1)
		// j - (j+1) becomes j - (k+1)
		// k - (k+1) becomes k - (i+1)

		int[] first = new int[this.path.length];

		for(int a=0; a<=i; a++){
			first[a] = path[a];
		}

		for(int a=i+1; a<=j; a++){
			first[a] = path[a - i + j];
		}

		for(int a=j+1; a<=k; a++){
			first[a] = path[a - j + i];
		}

		for(int a=k+1; a<this.path.length; a++){
			first[a] = path[a];
		}

		int[] second = new int[this.path.length];

		for(int a = 0; a<=i; a++){
			second[a] = this.path[a];
		}

		for(int a = i+1; a<=j; a++){
			second[a] = this.path[j - a + 1];
		}

		for(int a=j+1; a<=k; a++){
			second[a] = this.path[k+1 - a + j];
		}

		for(int a=k+1; a<this.path.length; a++){
			second[a] = this.path[a];
		}

		return new SwappablePath[]{
			new SwappablePath(first),
			new SwappablePath(second),
		};

	}

	public SwappablePath doubleBridge(int A, int B, int C){
		assert(A < B && B < C);
		assert(B-A == C-B);


		int[] fp = new int[path.length];
		int pos = 0;
		System.arraycopy(path, 0, fp, pos, A);
		pos+= A;
		System.arraycopy(path, C, fp, pos, path.length - C);
		pos += path.length - C;
		System.arraycopy(path, B, fp, pos, C - B);
		pos += C-B;
		System.arraycopy(path, A, fp, pos, B - A);


		return new SwappablePath(fp);
	}

	public int[] getPathArr(){
		return path;
	}

	public int calculateDistance(TSPData data) {
		if(length != -1){
			return length;
		}

		int[][] distances = data.getDistances();

		int distance = 0;
		for(int i = 0; i+1<path.length; i++){
			int a = path[i];
			int b = path[i+1];

			distance += distances[a][b];
		}
		length = distance;
		return distance;
	}

	public int getLength() {
		return length;
	}
}
