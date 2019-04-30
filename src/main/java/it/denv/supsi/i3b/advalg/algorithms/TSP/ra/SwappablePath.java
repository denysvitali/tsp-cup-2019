package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SwappablePath {
	private final int[] path;
	private int length = -1;

	public SwappablePath(SwappablePath sp){
		this.path = new int[sp.path.length];

		this.length = sp.length;
		System.arraycopy(sp.path, 0, this.path, 0, sp.path.length);
	}

	public SwappablePath(int[] path){
		this.path = path;
	}

	public SwappablePath twoOptSwap(int i, int j) {
		if(j < i){
			SwappablePath p = shift(j+1);
			return p.twoOptSwap(i-j, path.length-1);
		}

		int[] mPath = Arrays.copyOf(this.path, this.path.length);
		System.arraycopy(path, 0, mPath, 0, mPath.length);

		int r = 0;
		for (int v = i; v <= j; v++) {
			mPath[v] = this.path[j - r];
			r++;
		}

		return new SwappablePath(mPath);
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
		return new SwappablePath(fp);
	}

	public int[] getPathArr(){
		return path;
	}

	public int calculateDistance(TSPData data) {
		int[][] distances = data.getDistances();

		int distance = 0;
		for(int i = 0; i+1<path.length; i++){
			int a = path[i];
			int b = path[i+1];

			distance += distances[a][b];
		}

		distance += distances[path[path.length-1]][path[0]];

		this.length = distance;
		return distance;
	}

	public int getLength() {
		return length;
	}

	public boolean isValid() {
		Set<Integer> cities = new HashSet<>();

		for (int value : path) {
			if (cities.contains(value)) {
				System.err.println(value + " already visited!");
				return false;
			}
			cities.add(value);
		}

		return true;
	}

	public SwappablePath shift(int offset) {
		int[] start = new int[offset];
		int[] newPath = new int[path.length];

		System.arraycopy(path, 0, start, 0, offset);
		System.arraycopy(path, offset,
				path, 0, path.length - offset);
		System.arraycopy(start, 0, newPath,
				path.length - offset, offset);
		System.arraycopy(path, offset, newPath, offset, path.length - offset);

		return new SwappablePath(newPath);
	}
}
