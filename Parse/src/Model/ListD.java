package Model;

public class ListD {

	private D[] distances;

	public ListD(D[] distances) {
		this.distances = distances;
	}

	public void set(D[] distances) {
		this.distances = distances;
	}

	public D[] getDistances() {
		return distances;
	}

	public D getDistance(int num) {
		return distances[num];
	}
	
	public D getById(int id) {
		D dVide = new D();
		D monD;
		int j;
		for (j = 0; j < distances.length; j++) {
			monD = distances[j];
			if (id == monD.getId()) {
				return monD;
			}
		}
		System.err.println("Je renvoie un D vide");
		return dVide;
	}
}
