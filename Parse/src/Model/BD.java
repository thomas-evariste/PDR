package Model;

public class BD {

	private int[] ids;
	private int distance;

	public BD() {
		distance = -1;
	}

	public BD(int distance) {
		this.distance = distance;
	}


	public BD(int distance, int[] ids) {
		this.distance = distance;
		this.ids = ids;
	}

	public void setIds(int[] id) {
		ids = id;
	}

	public int getId(int num) {
		return ids[num];
	}

	public int[] getIds() {
		return ids;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

}
