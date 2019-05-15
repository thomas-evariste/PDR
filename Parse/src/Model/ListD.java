package Model;

public class ListD {

	private D[] distances;
    
	public ListD(D[] distances){
		this.distances = distances;
    }
	
	public void set(D[] distances){
		this.distances = distances;
	}
	
	public D[] getDistances(){
		return distances;
	}
	
	public D getDistance(int num){
		return distances[num];
	}
}
