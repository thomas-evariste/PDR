package Model;

import java.util.*;

public class Tools {

	static List<Integer> jap;
	static List<Integer> ameriqueDuN;
	static List<Integer> ameriqueDuS;
	static List<Integer> antarct;

	public static int destination(int idCellule, Graphe graphe) { // Déplacement sur un voisin au hasard
		Random rand = new Random();
		int arrivee = graphe.getCelluleById(idCellule)
				.getVoisin(rand.nextInt(graphe.getCelluleById(idCellule).nbVoisins()));
		return arrivee;
	}


	public static Graphe splitContinent(Continent continent) { // Initialisation des continents
		Graphe graphe = new Graphe();
		Continent ameriqueDuNord = new Continent(0);
		Continent ameriqueDuSudAfrique = new Continent(1);
		Continent antarctique = new Continent(2);
		Continent europeAsieOceanie = new Continent(3);
		Continent japon = new Continent(4);
		Cellule cellule;
		int i;
		for (i = 0; i < continent.size(); i++) {
			cellule = continent.getCellule(i);
			int id = cellule.getId();

			if (ameriqueDuS.contains(id)) {
				ameriqueDuSudAfrique.addCellule(cellule);
			} else if (ameriqueDuN.contains(id)) {
				ameriqueDuNord.addCellule(cellule);
			} else if (antarct.contains(id)) {
				antarctique.addCellule(cellule);
			} else if (jap.contains(id)) {
				japon.addCellule(cellule);
			} else {
				europeAsieOceanie.addCellule(cellule);
			}
		}
		graphe.addContinent(ameriqueDuNord);
		graphe.addContinent(ameriqueDuSudAfrique);
		graphe.addContinent(antarctique);
		graphe.addContinent(europeAsieOceanie);
		graphe.addContinent(japon);

		return graphe;
	}

	public static void remplirListIDContinent() { // Suite de l'initialisation des continents
		Integer[] j = new Integer[] { 143, 149, 150 };
		Integer[] an = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23,
				27, 28, 29, 37, 38, 43, 46, 47, 48, 49 };
		Integer[] as = new Integer[] { 18, 24, 25, 26, 30, 31, 32, 33, 34, 35, 36, 39, 40, 41, 42, 44, 45, 50, 51, 54,
				55, 56, 60, 61, 62, 63, 64, 65, 66, 71, 72, 73, 74, 75, 76, 77, 83, 84, 85, 86, 87, 88, 95, 96 };
		Integer[] ant = new Integer[] { 57, 67, 78, 89, 97, 104, 113 };
		jap = Arrays.asList(j);
		ameriqueDuN = Arrays.asList(an);
		ameriqueDuS = Arrays.asList(as);
		antarct = Arrays.asList(ant);

	}

	/*
	 * static void miseAJourNonConquis(ArrayList<int> cellulesNonConquises, Graphe
	 * graphe){ ArrayList<Integer> supp = new ArrayList<Integer>(); for(Continent
	 * continent : grapheNonConquis.getContinents()){ for(Cellule cellule :
	 * continent.getCellules()){
	 * if((graphe.getCellule(cellule.getId()).getControl())!=-1){
	 * supp.add(cellule.getId()); } } } for(int id : supp){
	 * grapheNonConquis.removeCelluleById(id); } }
	 */

	/*
	 * static void miseAJourPossessions(ArrayList<int[]> listInfosTempsReel,Graphe
	 * graphe){ //Plus adapté à la nouvelle modélisation for(int i = 0; i <
	 * graphe.sizeCellule();i++ ){
	 * graphe.getCellule(i).setControl(listInfosTempsReel.get(graphe.getCellule(i).
	 * getId())[1]); } }
	 */

	public static void miseAJourContinent(Graphe graphe) {
		ArrayList<Integer> supp = new ArrayList<Integer>();
		Continent continent;
		int j;
		for (j = 0; j < graphe.size(); j++) {
			continent = graphe.getContinent(j);
			if (continent.verifPoss()) {
				supp.add(continent.getId());
			}
		}
		int s;
		for (j = 0; j < supp.size(); j++) {
			s = supp.get(j);
			graphe.removeContinentById(s);
		}
	}

	public static void miseAJourGraphe(Graphe graphe) {
		miseAJourContinent(graphe);
	}

	public static int takeRandom(ArrayList<Integer> liste) {
		int taille = liste.size();
		Random rand = new Random();
		int hasard = rand.nextInt(taille);
		return liste.get(hasard);
	}

	public static String nouveauPlacement(int nbMaxCree, int playerCount, ArrayList<Integer> cellulesNonConquises,
			Graphe graphe, int myId, boolean premierTour) {
		String sortie = "";
		if (playerCount == 2) {
			if (premierTour) {
				sortie = nouveauPlacement1v1PremierTour(nbMaxCree, cellulesNonConquises, graphe, myId);
			} else {
				sortie = nouveauPlacement1v1(nbMaxCree, cellulesNonConquises, graphe, myId);
			}

		} else {
			if (premierTour) {
				sortie = nouveauPlacementMultiPremierTour(nbMaxCree, cellulesNonConquises, graphe, myId);
			} else {
				sortie = nouveauPlacementMulti(nbMaxCree, cellulesNonConquises, graphe, myId);
				// sortie = nouveauPlacementMultiPremierTour(nbMaxCree, cellulesNonConquises,
				// graphe, myId);
			}

		}
		return sortie;
	}

	public static String nouveauPlacement1v1(int nbMaxCree, ArrayList<Integer> cellulesNonConquises, Graphe graphe,
			int myId) {
		String sortie = "";
		int compteur = 0;
		while (nbMaxCree != 0) {
			if ((!cellulesNonConquises.isEmpty()) && cellulesNonConquises.size() > compteur) { // Tant qu'il reste des
																								// cellules non
																								// conquises on se place
																								// dessus, on vise en
																								// prorité celles qui
																								// ont le plus de
																								// platinum (liste
																								// triée)
				sortie = sortie + " 1 " + String.valueOf(cellulesNonConquises.get(compteur));
				nbMaxCree--;
				compteur++;
			} else {
				sortie = sortie + " 1 " + String.valueOf(Tools.position(graphe, myId, true));
				nbMaxCree--;
			}
		}
		return sortie;
	}

	public static String nouveauPlacementMultiPremierTour(int nbMaxCree, ArrayList<Integer> cellulesNonConquises,
			Graphe graphe, int myId) {
		String sortie = "";
		// On place nos 10 robots autour des 10 plus grandes mines
		for (int i = 0; i < nbMaxCree; i++) {
			sortie = sortie + " 1 " + String.valueOf(Tools.takeProcheGrandeMine(cellulesNonConquises, i, graphe));
		}
		return sortie;
	}

	public static ArrayList<Integer> triCellulesNonConquises(ArrayList<Integer> cellulesNonConquises, Graphe graphe) {
		ArrayList<Integer> tableauTrie = new ArrayList<Integer>();
		int i;
		int j;
		for (i = 0; i < 7; i++) {
			for (j = 0; j < cellulesNonConquises.size(); j++) {
				if (graphe.getCelluleById(cellulesNonConquises.get(j)).getPlatinum() == (6 - i)) {
					tableauTrie.add(cellulesNonConquises.get(j));
				}
			}
		}
		return tableauTrie;
	}

	public static int position(Graphe graphe, int myId, Boolean bool) { // On récupère une cellule au hasard sur
																				// le graphe
		Graphe grapheLocal = new Graphe(graphe);
		if (bool) {
			for (Continent continent : graphe.getContinents()) {
				if (!continent.estExploitable(myId)) {
					grapheLocal.removeContinentById(continent.getId());
				}
			}
		}
		ArrayList<Integer> mesCellules = new ArrayList<Integer>();
		for (Continent continent : grapheLocal.getContinents()) {
			for (Cellule cellule : continent.getCellules()) {
				if (cellule.getControl() == myId) {
					mesCellules.add(cellule.getId());
				}
			}
		}
		
		ArrayList<Integer> voisins = new ArrayList<Integer>();
		ArrayList<Integer> mesCellulesEnBordure = new ArrayList<Integer>();
		Boolean unVoisinEnnemi = false;
		for(int i=0; i<mesCellules.size();i++) {
			unVoisinEnnemi = false;
			voisins = graphe.getCelluleById(mesCellules.get(i)).getVoisins();
			for(int voisin : voisins) {
				if(graphe.getCelluleById(voisin).getControl() != myId) {
					unVoisinEnnemi = true;
				}
			}
			if(unVoisinEnnemi) {
				mesCellulesEnBordure.add(mesCellules.get(i));
			}
			
		}
		
		if(!mesCellulesEnBordure.isEmpty()) {
			mesCellules = mesCellulesEnBordure;
		}
		
		
		Random rand = new Random();
		int id = mesCellules.get(rand.nextInt(mesCellules.size()));
		return id;
	}

	// Pour n ième plus grande mine non conquise (n = parcourtNonConquis) on trouve
	// un voisin libre et on s'y implante si ce n'est pas possible on se place sur
	// la mine directement
	public static int takeProcheGrandeMine(ArrayList<Integer> cellulesNonConquises, int parcourtNonConquis,
			Graphe graphe) {
		Cellule maCellule = graphe.getCelluleById(cellulesNonConquises.get(parcourtNonConquis));
		for (int voisin : maCellule.getVoisins()) {
			if (graphe.getCelluleById(voisin).getControl() == -1) {
				return voisin;
			}
		}
		return maCellule.getId();
	}

	public static String nouveauPlacement1v1PremierTour(int nbMaxCree, ArrayList<Integer> cellulesNonConquises,
			Graphe graphe, int myId) {
		String sortie = "";
		int compteur = 0;
		for (int i = 0; i < 3; i++) {
			sortie = sortie + " 2 " + String.valueOf(cellulesNonConquises.get(i));
		}
		for (int i = 3; i < 7; i++) {
			sortie = sortie + " 1 " + String.valueOf(cellulesNonConquises.get(i));
		}
		return sortie;
	}

	public static String nouveauPlacementMulti(int nbMaxCree, ArrayList<Integer> cellulesNonConquises, Graphe graphe,
			int myId) {
		String sortie = "";
		int parcourtNonConquis = 0;
		for (int i = 0; i < nbMaxCree; i++) {

			// Si toutes les cellules sont possédées on se place aléatoirement sur l'une
			// des notres hormis sur un continent inexploitable.

			if (cellulesNonConquises.isEmpty()) {
				sortie = sortie + " 1 " + String.valueOf(Tools.position(graphe, myId, true));
			}
			// On se place sur les meilleurs mines non conquises, si on a plus de robots que
			// de cellules non conquises les meilleures pourront en recevoir plusieurs
			if (parcourtNonConquis >= cellulesNonConquises.size()) {
				parcourtNonConquis = 0;
			}

			else {
				sortie = sortie + " 1 "
						+ String.valueOf(Tools.takeProcheGrandeMine(cellulesNonConquises, parcourtNonConquis, graphe));
				parcourtNonConquis++;
			}

		}
		return sortie;
	}

	// Permet de créer la liste des cellules non conquises du meilleur continent au
	// moins bon, cela nous permettra d'avoir les cellules classées par platinum
	// puis par densité du continent
	public static ArrayList<Integer> CreeCellulesNonConquisesParContinent(Graphe graphe) {
		ArrayList<Integer> cellulesNonConquises = new ArrayList<Integer>();
		ArrayList<Integer> idContinentsClasses = new ArrayList<Integer>();
		ArrayList<Integer> idContinents = new ArrayList<Integer>();
		idContinents.add(0);
		idContinents.add(1);
		idContinents.add(2);
		idContinents.add(3);
		idContinents.add(4);
		double max = -1;
		int idContinentMax = -1;
		int compteur = 0;
		int compteurFinal = -1;
		while (!idContinents.isEmpty()) {
			for (int n : idContinents) {
				if (graphe.getContinentById(n).getDensitePlatinum() >= max) {
					idContinentMax = n;
					max = graphe.getContinentById(n).getDensitePlatinum();
					compteurFinal = compteur;
				}
				compteur++;
			}
			idContinentsClasses.add(idContinentMax);
			System.err.println("" + idContinentMax);
			idContinents.remove(compteurFinal);
			max = -1;
			idContinentMax = -1;
			compteur = 0;
			compteurFinal = -1;

		}

		for (int idContinent : idContinentsClasses) {
			for (Cellule uneCellule : graphe.getContinentById(idContinent).getCellules()) {
				cellulesNonConquises.add(uneCellule.getId());
			}
		}

		return cellulesNonConquises;
	}

}
