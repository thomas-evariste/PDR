package Model;

import java.util.*;

public class ListDistance {
	private ArrayList<Distance> distances;
    
	public ListDistance(){
		distances = new ArrayList<Distance>();
    }
	
	public void add(Distance distance){
		distances.add(distance);
	}
	
	public ArrayList<Distance> getDistances(){
		return distances;
	}
	
	public Distance getDistance(int num){
		return distances.get(num);
	}
}
