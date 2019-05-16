package Model;

public class D {

	private int id;
	private int[][] dists;

	public D() {
		id = -1;
		dists = new int[0][0];
	}

	public D(int id) {
		this.id = id;
	}

	public D(int id, int[][] dists) {
		this.id = id;
		this.dists = dists;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDists(int[][] dists) {
		this.dists = dists;
	}

	public int[][] getDists() {
		return dists;
	}

	public int[] getDist(int num) {
		return dists[num];
	}

	public int getId() {
		return id;
	}

	public int size() {
		return dists.length;
	}

}
