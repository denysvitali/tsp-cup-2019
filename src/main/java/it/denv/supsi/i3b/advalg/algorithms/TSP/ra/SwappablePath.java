package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SwappablePath {
	private int[] path;
	private int length = -1;

	public SwappablePath(SwappablePath sp){
		this.path = new int[sp.path.length];

		this.length = sp.length;
		System.arraycopy(sp.path, 0, this.path, 0, sp.path.length);
	}

	public SwappablePath(int[] path){
		this.path = path;
	}

	public void twoOptSwap(int i1, int k){
		if(i1 == k){
			return;
		}

		if(i1 < k){
			while(i1 < k){
				int temp = path[i1];
				path[i1] = path[k];
				path[k] = temp;
				i1++; k--;
			}
		} else {
			int offset = i1 - k;
			shift(offset);
			twoOptSwap(0, i1 - k);
		}

		length = -1;
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

		length = -1;

		return new SwappablePath[]{
			new SwappablePath(first),
			new SwappablePath(second),
		};

	}

	public SwappablePath doubleBridge(int[] cutPoints) {
		int[] fp = new int[path.length];
		int pos = 0;

		/*
			OLD:
			A-B-C-D

			NEW:
			A-D-C-B
		 */
		assert (cutPoints.length == 4);
		Arrays.sort(cutPoints);

		assert (cutPoints[0] > 0);
		assert (cutPoints[3] < path.length);

		int A = cutPoints[0];
		int B = cutPoints[1];
		int C = cutPoints[2];
		int D = cutPoints[3];

		System.arraycopy(path, 0, fp, pos, A);
		pos += A;

		System.arraycopy(path, C, fp, pos, D - C);
		pos += D - C;

		System.arraycopy(path, B, fp, pos, C - B);
		pos += C -B;

		System.arraycopy(path, A, fp, pos, B - A);
		pos += B - A;

		System.arraycopy(path, D, fp, pos, path.length - D);
		length = -1;

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

		distance += distances[path.length-1][0];

		length = distance;
		return distance;
	}

	public int getLength() {
		return length;
	}

	public boolean isValid() {
		Set<Integer> cities = new HashSet<>();

		for (int i=0; i<path.length; i++) {
			if(cities.contains(path[i])){
				System.err.println(path[i] + " already visited!");
				return false;
			}
			cities.add(path[i]);
		}

		return true;
	}

	public void shift(int offset) {
		int[] start = new int[offset];

		System.arraycopy(path, 0, start, 0, offset);
		System.arraycopy(path, offset,
				path, 0, path.length - offset);
		System.arraycopy(start, 0, path,
				path.length - offset, offset);
	}
}
