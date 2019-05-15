package Model;

import java.util.*;

public class Continent {
	private ArrayList<Cellule> cellules;
	private int id;
	private double densitePlatinum;

	public Continent() {
		cellules = new ArrayList<Cellule>();
		id = -1;
		densitePlatinum = 0;
	}

	public Continent(int id) {
		cellules = new ArrayList<Cellule>();
		this.id = id;
		densitePlatinum = 0;
	}

	public Continent(ArrayList<Cellule> cellules) {
		this.cellules = cellules;
		id = -1;
		densitePlatinum = 0;
	}

	public Continent(ArrayList<Cellule> cellules, int id) {
		this.cellules = cellules;
		this.id = id;
		densitePlatinum = 0;
	}

	public Continent(Continent continent) {
		cellules = new ArrayList<Cellule>(continent.getCellules());
		id = continent.getId();
		densitePlatinum = 0;
	}

	public ArrayList<Cellule> getCellules() {
		return cellules;
	}

	public Cellule getCellule(int num) {
		return cellules.get(num);
	}

	public double getDensitePlatinum() {
		return densitePlatinum;
	}

	public Cellule getCelluleById(int id) {
		Cellule celluleVide = new Cellule();
		Cellule cellule;
		int j;
		for (j = 0; j < cellules.size(); j++) {
			cellule = cellules.get(j);
			if (id == cellule.getId()) {
				return cellule;
			}
		}
		return celluleVide;
	}

	public void setCellules(ArrayList<Cellule> cellules) {
		this.cellules = cellules;
	}

	public void setDensitePlatinum(double d) {
		densitePlatinum = d;
	}

	public void addCellule(Cellule cellule) {
		cellules.add(cellule);
	}

	public void addCellules(ArrayList<Cellule> cellules) {
		Cellule cellule;
		int j;
		for (j = 0; j < cellules.size(); j++) {
			cellule = cellules.get(j);
			this.addCellule(cellule);
		}
	}

	public void removeCellule(int num) {
		cellules.remove(num);
	}

	public void removeCelluleById(int id) {
		int i;
		for (i = 0; i < cellules.size(); i++) {
			if (cellules.get(i).getId() == id) {
				cellules.remove(i);
				i--;
			}
		}
	}

	public int size() {
		return cellules.size();
	}

	public Boolean isEmpty() {
		return cellules.isEmpty();
	}

	public void triVoisinDe(int id) { // Classe les voisins du plus chargé en platinum
		// au moins chargé en platinum // Pas opti à
		// changer en cas de limite de temps
		ArrayList<Integer> voisin = this.getCelluleById(id).getVoisins();
		ArrayList<Integer> voisinTrie = new ArrayList<Integer>();
		int i;
		int j;
		for (i = 0; i < 7; i++) {
			for (j = voisin.size()-1; j >= 0; j--) {
				if (this.getCelluleById(voisin.get(j)).getPlatinum() == (6 - i)) {
					voisinTrie.add(voisin.get(j));
				}
			}
		}
		this.getCelluleById(id).setVoisins(voisinTrie);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Boolean verifPoss() {
		int i;
		for (i = 0; i < cellules.size() - 1; i++) {
			if (cellules.get(i).getControl() != cellules.get(i + 1).getControl()) {
				return false;
			}
		}
		if (cellules.get(0).getControl() == -1) {
			return false;
		}
		return true;
	}

	public void calculDensitePlatinum() {
		double comptPlatinum = 0;
		Cellule cellule;
		int j;
		for (j = 0; j < cellules.size(); j++) {
			cellule = cellules.get(j);
			comptPlatinum += cellule.getPlatinum();
		}

		double densitePlatinum = comptPlatinum / ((double) cellules.size());
		this.densitePlatinum = densitePlatinum;

	}

	public void triParPlatinum() {
		ArrayList<Cellule> cellulesTriees = new ArrayList<Cellule>();
		int i;
		int j;
		Cellule cellule;
		for (i = 0; i < 7; i++) {
			for (j = 0; j < cellules.size(); j++) {
				cellule = cellules.get(j);
				if (cellule.getPlatinum() == (6 - i)) {
					cellulesTriees.add(cellule);
				}
			}
		}
		cellules = cellulesTriees;
	}

	public int nombreDeCellulesControlees() {
		int compteur = 0;
		for (int i = 0; i < cellules.size(); i++) {
			if (getCelluleById(i).getControl() != -1) {
				compteur++;
			}
		}
		return compteur;
	}
	
	public Boolean estExploitable(int myId){
		int compteur[] = new int[2];
		for(int i=0;i<2;i++) {
			compteur[i]=0;
		}
		for(int j=0; j<cellules.size();j++) {
			if(getCellule(j).getControl() !=-1) {
				if(getCellule(j).getControl() == myId) {
					compteur[0]++;
				}
				else {
					compteur[1]++;
				}
			}
		}
		for(int k=0;k<2;k++) {
			if(compteur[k]==cellules.size()) {
				return false;
			}
		}
		return true;
	}
}
