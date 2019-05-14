package Model;

import java.util.*;

public class Cellule {

	private int id;
	private int controlePar;
	private int[] robots;
	private int platinum;
	private ArrayList<Integer> voisins;

	public Cellule(int id, int platinum, ArrayList<Integer> voisins) {
		this.id = id;
		this.platinum = platinum;
		this.voisins = voisins;
		controlePar = -1;
		robots = new int[4];
		int i;
		for (i = 0; i < 4; i++) {
			robots[i] = 0;
		}
	}

	public Cellule(int id, int platinum) {
		this.id = id;
		this.platinum = platinum;
		voisins = new ArrayList<Integer>();
		controlePar = -1;
		robots = new int[4];
		int i;
		for (i = 0; i < 4; i++) {
			robots[i] = 0;
		}

	}

	public Cellule() {
		id = -1;
		platinum = -1;
		voisins = new ArrayList<Integer>();
		controlePar = -1;
		robots = new int[4];
		int i;
		for (i = 0; i < 4; i++) {
			robots[i] = 0;
		}
	}

	public int getId() {
		return id;
	}

	public int getControl() {
		return controlePar;
	}

	public int getPlatinum() {
		return platinum;
	}

	public ArrayList<Integer> getVoisins() {
		return voisins;
	}

	public int getVoisin(int num) {
		return voisins.get(num);
	}

	public int getRobots(int i) {
		return robots[i];
	}

	public Boolean verifVoisin(int id) {
		return voisins.contains(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setControl(int numJ) {
		controlePar = numJ;
	}

	public void setPlatinum(int platinum) {
		this.platinum = platinum;
	}

	public void setVoisins(ArrayList<Integer> voisins) {
		this.voisins = voisins;
	}

	public void setRobots(int nombre, int i) {
		this.robots[i] = nombre;
	}

	public void addVoisin(int idVoisin) {
		voisins.add(idVoisin);
	}

	public void addVoisinA(int idVoisin, int pos) {
		voisins.add(pos, idVoisin);
	}

	public void suppVoisin(int pos) {
		voisins.remove(pos);
	}

	public void videVoisin() {
		voisins.clear();
	}

	public int nbVoisins() {
		return voisins.size();
	}

}