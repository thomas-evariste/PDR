package Model;

import java.util.*;

public class Graphe {
    private ArrayList<Continent> continents;
    
    public Graphe(){
        continents = new ArrayList<Continent>();
    }
    
    public Graphe(Graphe graphe){
        continents = new ArrayList<Continent>();
		Continent continent;
		int j;
        for(j=0;j<graphe.size();j++){
			continent=graphe.getContinent(j);
            continents.add(new Continent(continent));
        }
    }
    
    public ArrayList<Continent> getContinents(){
        return continents;
    }
    
    public Continent getContinent(int num){
        return continents.get(num);
    }
    
    public Continent getContinentById(int id){
        Continent continentVide = new Continent();
		Continent continent;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
            if(id==continent.getId()){
                return continent;
            }
        }
        System.err.println("continent pas dans le graphe: " + id);
        return continentVide;
    } 
    
    
    public void setContinents(ArrayList<Continent> continents){
        this.continents = new ArrayList<Continent>(continents);
    }
    
    public void addContinent(Continent continent){
        continents.add(continent);
    }
    
    public void addContinents(ArrayList<Continent> continents){
		Continent continent;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
            this.addContinent(continent);
        }
    }
    
    public void removeContinent(int num){
        continents.remove(num);
    }
    
    public void removeContinentById(int id){
		int i;
        for(i = 0; i< continents.size();i++){
            if(continents.get(i).getId()==id){
                continents.remove(i);
                i--;
            }
        }
    }
    
    public int size(){
        return continents.size();
    }
    
    public int sizeCellule(){
        int taille = 0;
		Continent continent;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
            taille += continent.size();
        }
        return taille;
    }
    
    public Boolean isEmptyContinent(){
        return continents.isEmpty();
    }
    
    public Boolean isEmptyCase(){
		Continent continent;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
            if(!continent.isEmpty()){
                return false;
            }
        }
        return true;
    }
    
    public Cellule getCelluleById(int id){
        Cellule celluleVide = new Cellule();
		Continent continent;
		Cellule cellule;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
			int k;
			for(k=0;k<continent.size();k++){
				cellule=continent.getCellule(k);
                if(id==cellule.getId()){
                    return cellule;
                }
            }
        }
        System.err.println("case pas dans le graphe: " + id);
        return celluleVide;
    }
    
    public Cellule getCellule(int num){
        Cellule celluleVide = new Cellule();
        int comptNum = 0;
		Continent continent;
		Cellule cellule;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
			int k;
			for(k=0;k<continent.size();k++){
				cellule=continent.getCellule(k);
                if(num==comptNum){
                    return cellule;
                }
                comptNum++;
            }
        }
        System.err.println("pas assez de cellules: " + num);
        return celluleVide;
    }
    
    public void removeCelluleById(int id){
        int idContinent = -1;
		Continent continent;
		Cellule cellule;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
			int k;
			for(k=0;k<continent.size();k++){
				cellule=continent.getCellule(k);
                if(id==cellule.getId()){
                    idContinent = continent.getId();
                }
            }
        }
        continents.get(idContinent).removeCelluleById(id);
    }
}
