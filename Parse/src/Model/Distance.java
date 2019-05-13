package Model;

public class Distance {

	private int id1;
	private int id2; // on prend id1 < id2 comme convention
	private int dist;
	
	public Distance(){
		id1=-1;
		id2=-1;
		dist=-1;
	}
	
	public Distance(int id1, int id2){
		this.id1=min(id1,id2);
		this.id2=max(id1,id2);
		dist=-1;
	}
	
	public Distance(int id1, int id2, int dist){
		this.id1=min(id1,id2);
		this.id2=max(id1,id2);
		this.dist=dist;
	}

	public void setId1(int id){
		if(id2!=-1){
			if(id>id2){
				id1=id2;
				id2=id;
			}
			else{
				id1=id;
			}
		}
		else{
			id1=id;
		}
	}

	public void setId2(int id){
		if(id1!=-1){
			if(id<id1){
				id2=id1;
				id1=id;
			}
			else{
				id1=id;
			}
		}
		else{
			id1=id;
		}
	}
	
	public void setIds(int id1, int id2){
		this.id1=min(id1,id2);
		this.id2=max(id1,id2);
	}
	
	public void setDist(int dist){
		this.dist=dist;
	}
	
	public int getDist(){
		return dist;
	}
	
	public int getId1(){
		return id1;
	}
	
	public int getId2(){
		return id2;
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
