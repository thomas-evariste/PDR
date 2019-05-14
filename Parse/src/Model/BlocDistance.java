package Model;

import java.util.*;

public class BlocDistance {

	private ArrayList<Integer> ids;
	private int distance;

	public BlocDistance(){
		ids = new ArrayList<Integer>();
		distance=-1;
	}

	public BlocDistance(int distance){
		ids = new ArrayList<Integer>();
		this.distance=distance;
	}
	
	public void add(int id){
		ids.add(id);
	}
	
	public int getId(int num){
		return ids.get(num);
	}
	
	public ArrayList<Integer> getIds(){
		return ids;
	}
	
	public void setDistance(int distance){
		this.distance=distance;
	}
	
	public int getDistance(){
		return distance;
	}
	
	
}
