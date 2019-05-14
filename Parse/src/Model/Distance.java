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
		BlocDistance dist = new BlocDistance(distance);
		dists.add(dist);
		return dist;
	}
	
	public int getId(){
		return id;
	}
	
	public void triBlocList(){
		ArrayList<BlocDistance> newDists= new ArrayList<BlocDistance>();
		for(int i=0;i<dists.size();i++){
			for(int j=0;j<dists.size();j++){
				if(i==dists.get(j).getDistance()){
					newDists.add(dists.get(j));
					break;
				}
			}
		}
		dists = newDists;
	}
	
	public int size(){
		return dists.size();
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
