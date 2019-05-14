package Model;

import java.util.*;

public class Distance {

	private int id;
	private ArrayList<BlocDistance> dists;
	
	public Distance(){
		id=-1;
		dists= new ArrayList<BlocDistance>();
	}
	
	public Distance(int id){
		this.id=id;
		dists= new ArrayList<BlocDistance>();
	}
	
	public Distance(int id, ArrayList<BlocDistance> dists){
		this.id=id;
		this.dists=dists;
	}

	public void setId(int id){
		this.id=id;
	}

	
	public void setDists(ArrayList<BlocDistance> dists){
		this.dists=dists;
	}
	
	public void add(BlocDistance dist){
		dists.add(dist);
	}
	
	public ArrayList<BlocDistance> getDists(){
		return dists;
	}
	
	public BlocDistance getDist(int num){
		return dists.get(num);
	}
	
	public BlocDistance getDistByDistance(int distance){
		for(BlocDistance dist : dists){
			if(dist.getDistance()==distance){
				return dist;
			}
		}
		BlocDistance dist = new BlocDistance();
		dist.setDistance(distance);
		dists.add(dist);
		return dist;
	}
	
	public int getId(){
		return id;
	}
	
	
	
	
	public int min(int a,int b){
		if(a<b)
			return a;
		return b;
	}
	
	public int max(int a,int b){
		if(a>b)
			return a;
		return b;
	}
	
}
