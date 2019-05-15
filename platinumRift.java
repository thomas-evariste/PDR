import java.util.*;


class Player {

	public static void main(String args[]) {

		/////////////////////// INITIALISATION
		/////////////////////// ////////////////////////////////////////////
		ArrayList<Integer> cellulesNonConquises = new ArrayList<Integer>();
		int i;
		int j;
		int k;
		int l;

		Tools.remplirListIDContinent();
		Scanner in = new Scanner(System.in);
		int playerCount = in.nextInt(); // Nombre de joueurs (2 à 4)
		int myId = in.nextInt(); // Notre ID
		int zoneCount = in.nextInt(); // Nombre de cellules
		int linkCount = in.nextInt(); // Nombre de frontières
		int zoneId;
		int platinumSource;
		Continent pangee = new Continent();

		// Création des cellules et ajout à la pangée
		for (i = 0; i < zoneCount; i++) {
			zoneId = in.nextInt(); // On récupère un ID de cellule
			//cellulesNonConquises.add(zoneId);
			platinumSource = in.nextInt(); // Et son nombre de platinum
			Cellule cellule = new Cellule(zoneId, platinumSource);
			pangee.addCellule(cellule);
		}

		int zone1;
		int zone2;
		// Mise en place des voisins
		for (i = 0; i < linkCount; i++) {
			zone1 = in.nextInt();
			zone2 = in.nextInt();
			pangee.getCelluleById(zone1).addVoisin(zone2);
			pangee.getCelluleById(zone2).addVoisin(zone1);
		}

		// On trie les voisins de chaque cellule du plus au moins chargé en
		// platinum
		for (i = 0; i < pangee.size(); i++) {
			pangee.triVoisinDe(i);
		}
		
		Graphe graphe = Tools.splitContinent(pangee);


		for (Continent continent : graphe.getContinents()) {
			continent.calculDensitePlatinum();
			continent.triParPlatinum();
		}
		
		cellulesNonConquises = Tools.CreeCellulesNonConquisesParContinent(graphe);
		cellulesNonConquises = Tools.triCellulesNonConquises(cellulesNonConquises, graphe);

		//////////////// PROCEDURE A CHAQUE TOUR
		//////////////// /////////////////////////////////////////

		int zID; // L'ID de la cellule qu'on manipulera
		int myPlatinum;
		String deplacementStr;
		Cellule cellule;
		List<Integer> deplacement = new ArrayList<Integer>();
		int arrivee;
		int voisin;
		int nbMaxCree;
		String placement;
		Continent continent;
		boolean premierTour = true;
		while (true) {

			////// PHASE DE RECUPERATION DE DONNEES //////

			myPlatinum = in.nextInt(); // Mon platinum
			for (i = 0; i < zoneCount; i++) {

				zID = in.nextInt(); // On récupère l'ID de la cellule
				graphe.getCelluleById(i).setControl(in.nextInt());
				if ((graphe.getCelluleById(i).getControl() != -1)) {
					for (j = 0; j < cellulesNonConquises.size(); j++) {
						if (i == cellulesNonConquises.get(j)) {
							cellulesNonConquises.remove(j);
							break;
						}
					}

				}
				for (j = 0; j < 4; j++) {
					graphe.getCelluleById(i).setRobots(in.nextInt(), j);
				}
			}
			
			

			////////// PHASE DE DEPLACEMENT ////////////

			deplacementStr = "";

			for (j = 0; j < graphe.size(); j++) {
				continent = graphe.getContinent(j);
				for (k = 0; k < continent.size(); k++) {
					cellule = continent.getCellule(k);
					// Si on a au moins un robot disponible sur la cellule
					if (cellule.getRobots(myId) != 0) {
						deplacement.clear();
						// Pour chaque robot
						for (i = 0; i < cellule.getRobots(myId); i++) {

							arrivee = Tools.destination(cellule.getId(), graphe); // On
																					// récupère
																					// un
																					// voisin
																					// aléatoirement
							// Pour chaque voisin de notre cellule
							for (l = 0; l < cellule.nbVoisins(); l++) {
								voisin = cellule.getVoisin(l);
								// Si nous ne sommes pas propriétaire de la
								// meilleure cellule voisine et qu'un autre
								// robot venant de la cellule de départ n'a pas
								// déjà fait ce déplacement on change l'arrivée
								if ((graphe.getCelluleById(voisin).getControl() != myId)
										&& (!deplacement.contains(voisin))) {
									arrivee = voisin;
									deplacement.add(arrivee);
									break;
								}
							}

							deplacementStr = deplacementStr + " " + 1 + " " + cellule.getId() + " " + arrivee; // On
																												// déplace
																												// un
																												// robot
																												// à
																												// la
																												// fois

						}
					}
				}
			}

			if (deplacementStr == "") {
				deplacementStr = "WAIT";
			}
			System.out.println(deplacementStr);

			/////////// PHASE D'ACHAT ET PLACEMENT DE NOUVEAUX ROBOTS
			/////////// ///////////////

			nbMaxCree = myPlatinum / 20;
			placement = "";
			placement = Tools.nouveauPlacement(nbMaxCree, playerCount, cellulesNonConquises, graphe, myId, premierTour);
			if (placement == "") {
				placement = "WAIT";
			}

			System.out.println(placement);
			
			if(premierTour) {
				premierTour = false;
			}
		}
	}

}


public class Tools {

	static List<Integer> jap;
	static List<Integer> ameriqueDuN;
	static List<Integer> ameriqueDuS;
	static List<Integer> antarct;

	public static int destination(int idCellule, Graphe graphe) { // Déplacement
																	// sur un
																	// voisin au
																	// hasard
		Random rand = new Random();
		int arrivee = graphe.getCelluleById(idCellule)
				.getVoisin(rand.nextInt(graphe.getCelluleById(idCellule).nbVoisins()));
		return arrivee;
	}

	public static int positionAlea(Graphe graphe) { // On récupère une cellule
													// au hasard sur le graphe
		Random rand = new Random();
		int pos = graphe.getCellule(rand.nextInt(graphe.sizeCellule())).getId();
		return pos;
	}

	// static int[] classementCellulesDepart(Graphe graphe){}

	public static Graphe splitContinent(Continent continent) { // Initialisation
																// des
																// continents
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

	public static void remplirListIDContinent() { // Suite de l'initialisation
													// des continents
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
	 * static void miseAJourNonConquis(ArrayList<int> cellulesNonConquises,
	 * Graphe graphe){ ArrayList<Integer> supp = new ArrayList<Integer>();
	 * for(Continent continent : grapheNonConquis.getContinents()){ for(Cellule
	 * cellule : continent.getCellules()){
	 * if((graphe.getCellule(cellule.getId()).getControl())!=-1){
	 * supp.add(cellule.getId()); } } } for(int id : supp){
	 * grapheNonConquis.removeCelluleById(id); } }
	 */

	/*
	 * static void miseAJourPossessions(ArrayList<int[]>
	 * listInfosTempsReel,Graphe graphe){ //Plus adapté à la nouvelle
	 * modélisation for(int i = 0; i < graphe.sizeCellule();i++ ){
	 * graphe.getCellule(i).setControl(listInfosTempsReel.get(graphe.getCellule(
	 * i). getId())[1]); } }
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
				if (premierTour) {
					sortie = nouveauPlacementMultiPremierTour(nbMaxCree, cellulesNonConquises, graphe, myId);
				} else {
					sortie = nouveauPlacementMulti(nbMaxCree, cellulesNonConquises, graphe, myId);
				}
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
			if ((!cellulesNonConquises.isEmpty()) && cellulesNonConquises.size() > compteur) { // Tant
																								// qu'il
																								// reste
																								// des
																								// cellules
																								// non
																								// conquises
																								// on
																								// se
																								// place
																								// dessus,
																								// on
																								// vise
																								// en
																								// prorité
																								// celles
																								// qui
																								// ont
																								// le
																								// plus
																								// de
																								// platinum
																								// (liste
																								// triée)
				sortie = sortie + " 1 " + String.valueOf(cellulesNonConquises.get(compteur));
				nbMaxCree--;
				compteur++;
			} else {
				sortie = sortie + " 1 " + String.valueOf(Tools.positionAleaV2(graphe, myId, true));
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

	public static int positionAleaV2(Graphe graphe, int myId, Boolean bool) { // On
																				// récupère
																				// une
																				// cellule
																				// au
																				// hasard
																				// sur
																				// le
																				// graphe
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

		Random rand = new Random();
		int id = mesCellules.get(rand.nextInt(mesCellules.size()));
		return id;
	}

	// Pour n ième plus grande mine non conquise (n = parcourtNonConquis) on
	// trouve
	// un voisin libre et on s'y implante si ce n'est pas possible on se place
	// sur
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
				sortie = sortie + " 1 " + String.valueOf(Tools.positionAleaV2(graphe, myId, true));
			}
			// On se place sur les meilleurs mines non conquises, si on a plus de robots que
			// de cellules non conquises les meilleures pourront en recevoir plusieurs
			if (parcourtNonConquis >= cellulesNonConquises.size()) {
				parcourtNonConquis = 0;
			}

			else {
				sortie = sortie + " 1 " + String.valueOf(cellulesNonConquises.get(parcourtNonConquis));
				parcourtNonConquis++;
			}

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
				sortie = sortie + " 1 " + String.valueOf(Tools.positionAleaV2(graphe, myId, true));
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


class Graphe {
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
        System.err.println(""+ comptNum);
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


class Continent {
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

public class D {

	private int id;
	private int[][] dists;

	public D(){
		id=-1;
	}

	public D(int id){
		this.id=id;
	}

	public D(int id, int[][] dists){
		this.id=id;
		this.dists=dists;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDists(int[][] dists) {
		this.dists = dists;
	}

	public int[][] getDists() {
		return dists;
	}

	public int[] getDist(int num) {
		return dists[num];
	}

	public int getId() {
		return id;
	}


	public int size() {
		return dists.length;
	}

}
class CreerD {
public static ArrayList<ListD> creer() {
int[][] b;
ArrayList<ListD> list = new ArrayList<ListD>();
b={{150}+{149}};
D d0=new D(143,b);
b={{150}+{143}};
D d1=new D(149,b);
b={{143+149}};
D d2=new D(150,b);
D[] di={d0+d1+d2};
ListD listD=new ListD(di); 
list = list.add(listD) ;
b={{67}+{78}+{89}+{97}+{104}+{113}};
D d0=new D(57,b);
b={{57+78}+{89}+{97}+{104}+{113}};
D d1=new D(67,b);
b={{67+89}+{57+97}+{104}+{113}};
D d2=new D(78,b);
b={{78+97}+{67+104}+{57+113}};
D d3=new D(89,b);
b={{89+104}+{78+113}+{67}+{57}};
D d4=new D(97,b);
b={{97+113}+{89}+{78}+{67}+{57}};
D d5=new D(104,b);
b={{104}+{97}+{89}+{78}+{67}+{57}};
D d6=new D(113,b);
D[] di={d0+d1+d2+d3+d4+d5+d6};
ListD listD=new ListD(di); 
list = list.add(listD) ;
b={{1}+{2+3}+{4+7+8}+{5+9+13+14+15}+{6+10+16+19+20+21+22}+{11+17+23+27+28+29}+{12+37+38}+{43}+{46}+{47+48+49}};
D d0=new D(0,b);
b={{0+2+3}+{4+7+8}+{5+9+13+14+15}+{6+10+16+19+20+21+22}+{11+17+23+27+28+29}+{12+37+38}+{43}+{46}+{47+48+49}};
D d1=new D(1,b);
b={{1+3+7}+{0+4+8+13+14}+{5+9+15+19+20+21}+{6+10+16+22+27+28+29}+{11+17+23+37+38}+{12+43}+{46}+{47+48+49}};
D d2=new D(2,b);
b={{1+2+4+7+8}+{0+5+9+13+14+15}+{6+10+16+19+20+21+22}+{11+17+23+27+28+29}+{12+37+38}+{43}+{46}+{47+48+49}};
D d3=new D(3,b);
b={{3+5+8+9}+{1+2+6+7+10+14+15+16}+{0+11+13+17+20+21+22+23}+{12+19+27+28+29}+{37+38}+{43}+{46}+{47+48+49}};
D d4=new D(4,b);
b={{4+6+9+10}+{3+8+11+15+16+17}+{1+2+7+12+14+21+22+23}+{0+13+20+28+29}+{19+27+38}+{37}+{43}+{46}+{47+48+49}};
D d5=new D(5,b);
b={{5+10+11}+{4+9+12+16+17}+{3+8+15+22+23}+{1+2+7+14+21+29}+{0+13+20+28+38}+{19+27}+{37}+{43}+{46}+{47+48+49}};
D d6=new D(6,b);
b={{2+3+8+13+14}+{1+4+9+15+19+20+21}+{0+5+10+16+22+27+28+29}+{6+11+17+23+37+38}+{12+43}+{46}+{47+48+49}};
D d7=new D(7,b);
b={{3+4+7+9+14+15}+{1+2+5+10+13+16+20+21+22}+{0+6+11+17+19+23+27+28+29}+{12+37+38}+{43}+{46}+{47+48+49}};
D d8=new D(8,b);
b={{4+5+8+10+15+16}+{3+6+7+11+14+17+21+22+23}+{1+2+12+13+20+28+29}+{0+19+27+38}+{37}+{43}+{46}+{47+48+49}};
D d9=new D(9,b);
b={{5+6+9+11+16+17}+{4+8+12+15+22+23}+{3+7+14+21+29}+{1+2+13+20+28+38}+{0+19+27}+{37}+{43}+{46}+{47+48+49}};
D d10=new D(10,b);
b={{6+10+12+17}+{5+9+16+23}+{4+8+15+22}+{3+7+14+21+29}+{1+2+13+20+28+38}+{0+19+27}+{37}+{43}+{46}+{47+48+49}};
D d11=new D(11,b);
b={{11}+{6+10+17}+{5+9+16+23}+{4+8+15+22}+{3+7+14+21+29}+{1+2+13+20+28+38}+{0+19+27}+{37}+{43}+{46}+{47+48+49}};
D d12=new D(12,b);
b={{7+14+19+20}+{2+3+8+15+21+27+28}+{1+4+9+16+22+29+37+38}+{0+5+10+17+23+43}+{6+11+46}+{12+47+48+49}};
D d13=new D(13,b);
b={{7+8+13+15+20+21}+{2+3+4+9+16+19+22+27+28+29}+{1+5+10+17+23+37+38}+{0+6+11+43}+{12+46}+{47+48+49}};
D d14=new D(14,b);
b={{8+9+14+16+21+22}+{3+4+5+7+10+13+17+20+23+28+29}+{1+2+6+11+19+27+38}+{0+12+37}+{43}+{46}+{47+48+49}};
D d15=new D(15,b);
b={{9+10+15+17+22+23}+{4+5+6+8+11+14+21+29}+{3+7+12+13+20+28+38}+{1+2+19+27}+{0+37}+{43}+{46}+{47+48+49}};
D d16=new D(16,b);
b={{10+11+16+23}+{5+6+9+12+15+22}+{4+8+14+21+29}+{3+7+13+20+28+38}+{1+2+19+27}+{0+37}+{43}+{46}+{47+48+49}};
D d17=new D(17,b);
b={{13+20+27}+{7+14+21+28+37}+{2+3+8+15+22+29+38+43}+{1+4+9+16+23+46}+{0+5+10+17+47+48+49}+{6+11}+{12}};
D d18=new D(19,b);
b={{13+14+19+21+27+28}+{7+8+15+22+29+37+38}+{2+3+4+9+16+23+43}+{1+5+10+17+46}+{0+6+11+47+48+49}+{12}};
D d19=new D(20,b);
b={{14+15+20+22+28+29}+{7+8+9+13+16+19+23+27+38}+{2+3+4+5+10+17+37}+{1+6+11+43}+{0+12+46}+{47+48+49}};
D d20=new D(21,b);
b={{15+16+21+23+29}+{8+9+10+14+17+20+28+38}+{3+4+5+6+7+11+13+19+27}+{1+2+12+37}+{0+43}+{46}+{47+48+49}};
D d21=new D(22,b);
b={{16+17+22}+{9+10+11+15+21+29}+{4+5+6+8+12+14+20+28+38}+{3+7+13+19+27}+{1+2+37}+{0+43}+{46}+{47+48+49}};
D d22=new D(23,b);
b={{19+20+28+37}+{13+14+21+29+38+43}+{7+8+15+22+46}+{2+3+4+9+16+23+47+48+49}+{1+5+10+17}+{0+6+11}+{12}};
D d23=new D(27,b);
b={{20+21+27+29+38}+{13+14+15+19+22+37}+{7+8+9+16+23+43}+{2+3+4+5+10+17+46}+{1+6+11+47+48+49}+{0+12}};
D d24=new D(28,b);
b={{21+22+28+38}+{14+15+16+20+23+27}+{7+8+9+10+13+17+19+37}+{2+3+4+5+6+11+43}+{1+12+46}+{0+47+48+49}};
D d25=new D(29,b);
b={{27+43}+{19+20+28+46}+{13+14+21+29+38+47+48+49}+{7+8+15+22}+{2+3+4+9+16+23}+{1+5+10+17}+{0+6+11}+{12}};
D d26=new D(37,b);
b={{28+29}+{20+21+22+27}+{13+14+15+16+19+23+37}+{7+8+9+10+17+43}+{2+3+4+5+6+11+46}+{1+12+47+48+49}+{0}};
D d27=new D(38,b);
b={{37+46}+{27+47+48+49}+{19+20+28}+{13+14+21+29+38}+{7+8+15+22}+{2+3+4+9+16+23}+{1+5+10+17}+{0+6+11}+{12}};
D d28=new D(43,b);
b={{43+47+48+49}+{37}+{27}+{19+20+28}+{13+14+21+29+38}+{7+8+15+22}+{2+3+4+9+16+23}+{1+5+10+17}+{0+6+11}+{12}};
D d29=new D(46,b);
b={{46+49}+{43+48}+{37}+{27}+{19+20+28}+{13+14+21+29+38}+{7+8+15+22}+{2+3+4+9+16+23}+{1+5+10+17}+{0+6+11}+{12}};
D d30=new D(47,b);
b={{46+49}+{43+47}+{37}+{27}+{19+20+28}+{13+14+21+29+38}+{7+8+15+22}+{2+3+4+9+16+23}+{1+5+10+17}+{0+6+11}+{12}};
D d31=new D(48,b);
b={{46+47+48}+{43}+{37}+{27}+{19+20+28}+{13+14+21+29+38}+{7+8+15+22}+{2+3+4+9+16+23}+{1+5+10+17}+{0+6+11}+{12}};
D d32=new D(49,b);
D[] di={d0+d1+d2+d3+d4+d5+d6+d7+d8+d9+d10+d11+d12+d13+d14+d15+d16+d17+d18+d19+d20+d21+d22+d23+d24+d25+d26+d27+d28+d29+d30+d31+d32};
ListD listD=new ListD(di); 
list = list.add(listD) ;
b={{44+51}+{39+40+45+50+55+56}+{30+31+32+41+54+61+62+63}+{24+25+26+33+42+60+64+71+72+73+74}+{34+65+75+83+84+85+86}+{35+66+76+87+95+96}+{36+77+88}};
D d0=new D(18,b);
b={{25+30}+{26+31+39}+{32+40+44}+{18+33+41+45}+{34+42+51}+{35+50+55+56}+{36+54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d1=new D(24,b);
b={{24+26+30+31}+{32+39+40}+{33+41+44+45}+{18+34+42}+{35+51}+{36+50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d2=new D(25,b);
b={{25+31+32}+{24+30+33+39+40+41}+{34+42+44+45}+{18+35}+{36+51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d3=new D(26,b);
b={{24+25+31+39}+{26+32+40+44}+{18+33+41+45}+{34+42+51}+{35+50+55+56}+{36+54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d4=new D(30,b);
b={{25+26+30+32+39+40}+{24+33+41+44+45}+{18+34+42}+{35+51}+{36+50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d5=new D(31,b);
b={{26+31+33+40+41}+{25+30+34+39+42+44+45}+{18+24+35}+{36+51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d6=new D(32,b);
b={{32+34+41+42}+{26+31+35+40+45}+{25+30+36+39+44}+{18+24}+{51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d7=new D(33,b);
b={{33+35+42}+{32+36+41}+{26+31+40+45}+{25+30+39+44}+{18+24}+{51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d8=new D(34,b);
b={{34+36}+{33+42}+{32+41}+{26+31+40+45}+{25+30+39+44}+{18+24}+{51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d9=new D(35,b);
b={{35}+{34}+{33+42}+{32+41}+{26+31+40+45}+{25+30+39+44}+{18+24}+{51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d10=new D(36,b);
b={{30+31+40+44}+{18+24+25+26+32+41+45}+{33+42+51}+{34+50+55+56}+{35+54+61+62+63}+{36+60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d11=new D(39,b);
b={{31+32+39+41+44+45}+{18+25+26+30+33+42}+{24+34+51}+{35+50+55+56}+{36+54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d12=new D(40,b);
b={{32+33+40+42+45}+{26+31+34+39+44}+{18+25+30+35}+{24+36+51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d13=new D(41,b);
b={{33+34+41}+{32+35+40+45}+{26+31+36+39+44}+{18+25+30}+{24+51}+{50+55+56}+{54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d14=new D(42,b);
b={{18+39+40+45}+{30+31+32+41+51}+{24+25+26+33+42+50+55+56}+{34+54+61+62+63}+{35+60+64+71+72+73+74}+{36+65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d15=new D(44,b);
b={{40+41+44}+{18+31+32+33+39+42}+{25+26+30+34+51}+{24+35+50+55+56}+{36+54+61+62+63}+{60+64+71+72+73+74}+{65+75+83+84+85+86}+{66+76+87+95+96}+{77+88}};
D d16=new D(45,b);
b={{51+54+55}+{18+56+60+61+62}+{44+63+71+72+73}+{39+40+45+64+74+83+84+85}+{30+31+32+41+65+75+86+95+96}+{24+25+26+33+42+66+76+87}+{34+77+88}+{35}+{36}};
D d17=new D(50,b);
b={{18+50+55+56}+{44+54+61+62+63}+{39+40+45+60+64+71+72+73+74}+{30+31+32+41+65+75+83+84+85+86}+{24+25+26+33+42+66+76+87+95+96}+{34+77+88}+{35}+{36}};
D d18=new D(51,b);
b={{50+55+60+61}+{51+56+62+71+72}+{18+63+73+83+84}+{44+64+74+85+95+96}+{39+40+45+65+75+86}+{30+31+32+41+66+76+87}+{24+25+26+33+42+77+88}+{34}+{35}+{36}};
D d19=new D(54,b);
b={{50+51+54+56+61+62}+{18+60+63+71+72+73}+{44+64+74+83+84+85}+{39+40+45+65+75+86+95+96}+{30+31+32+41+66+76+87}+{24+25+26+33+42+77+88}+{34}+{35}+{36}};
D d20=new D(55,b);
b={{51+55+62+63}+{18+50+54+61+64+72+73+74}+{44+60+65+71+75+83+84+85+86}+{39+40+45+66+76+87+95+96}+{30+31+32+41+77+88}+{24+25+26+33+42}+{34}+{35}+{36}};
D d21=new D(56,b);
b={{54+61+71}+{50+55+62+72+83}+{51+56+63+73+84+95}+{18+64+74+85+96}+{44+65+75+86}+{39+40+45+66+76+87}+{30+31+32+41+77+88}+{24+25+26+33+42}+{34}+{35}+{36}};
D d22=new D(60,b);
b={{54+55+60+62+71+72}+{50+51+56+63+73+83+84}+{18+64+74+85+95+96}+{44+65+75+86}+{39+40+45+66+76+87}+{30+31+32+41+77+88}+{24+25+26+33+42}+{34}+{35}+{36}};
D d23=new D(61,b);
b={{55+56+61+63+72+73}+{50+51+54+60+64+71+74+83+84+85}+{18+65+75+86+95+96}+{44+66+76+87}+{39+40+45+77+88}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d24=new D(62,b);
b={{56+62+64+73+74}+{51+55+61+65+72+75+84+85+86}+{18+50+54+60+66+71+76+83+87+95+96}+{44+77+88}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d25=new D(63,b);
b={{63+65+74+75}+{56+62+66+73+76+85+86+87}+{51+55+61+72+77+84+88+96}+{18+50+54+60+71+83+95}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d26=new D(64,b);
b={{64+66+75+76}+{63+74+77+86+87+88}+{56+62+73+85}+{51+55+61+72+84+96}+{18+50+54+60+71+83+95}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d27=new D(65,b);
b={{65+76+77}+{64+75+87+88}+{63+74+86}+{56+62+73+85}+{51+55+61+72+84+96}+{18+50+54+60+71+83+95}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d28=new D(66,b);
b={{60+61+72+83}+{54+55+62+73+84+95}+{50+51+56+63+74+85+96}+{18+64+75+86}+{44+65+76+87}+{39+40+45+66+77+88}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d29=new D(71,b);
b={{61+62+71+73+83+84}+{54+55+56+60+63+74+85+95+96}+{50+51+64+75+86}+{18+65+76+87}+{44+66+77+88}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d30=new D(72,b);
b={{62+63+72+74+84+85}+{55+56+61+64+71+75+83+86+95+96}+{50+51+54+60+65+76+87}+{18+66+77+88}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d31=new D(73,b);
b={{63+64+73+75+85+86}+{56+62+65+72+76+84+87+96}+{51+55+61+66+71+77+83+88+95}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d32=new D(74,b);
b={{64+65+74+76+86+87}+{63+66+73+77+85+88}+{56+62+72+84+96}+{51+55+61+71+83+95}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d33=new D(75,b);
b={{65+66+75+77+87+88}+{64+74+86}+{63+73+85}+{56+62+72+84+96}+{51+55+61+71+83+95}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d34=new D(76,b);
b={{66+76+88}+{65+75+87}+{64+74+86}+{63+73+85}+{56+62+72+84+96}+{51+55+61+71+83+95}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d35=new D(77,b);
b={{71+72+84+95}+{60+61+62+73+85+96}+{54+55+56+63+74+86}+{50+51+64+75+87}+{18+65+76+88}+{44+66+77}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d36=new D(83,b);
b={{72+73+83+85+95+96}+{61+62+63+71+74+86}+{54+55+56+60+64+75+87}+{50+51+65+76+88}+{18+66+77}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d37=new D(84,b);
b={{73+74+84+86+96}+{62+63+64+72+75+83+87+95}+{55+56+61+65+71+76+88}+{50+51+54+60+66+77}+{18}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d38=new D(85,b);
b={{74+75+85+87}+{63+64+65+73+76+84+88+96}+{56+62+66+72+77+83+95}+{51+55+61+71}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d39=new D(86,b);
b={{75+76+86+88}+{64+65+66+74+77+85}+{63+73+84+96}+{56+62+72+83+95}+{51+55+61+71}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d40=new D(87,b);
b={{76+77+87}+{65+66+75+86}+{64+74+85}+{63+73+84+96}+{56+62+72+83+95}+{51+55+61+71}+{18+50+54+60}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d41=new D(88,b);
b={{83+84+96}+{71+72+73+85}+{60+61+62+63+74+86}+{54+55+56+64+75+87}+{50+51+65+76+88}+{18+66+77}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d42=new D(95,b);
b={{84+85+95}+{72+73+74+83+86}+{61+62+63+64+71+75+87}+{54+55+56+60+65+76+88}+{50+51+66+77}+{18}+{44}+{39+40+45}+{30+31+32+41}+{24+25+26+33+42}+{34}+{35}+{36}};
D d43=new D(96,b);
D[] di={d0+d1+d2+d3+d4+d5+d6+d7+d8+d9+d10+d11+d12+d13+d14+d15+d16+d17+d18+d19+d20+d21+d22+d23+d24+d25+d26+d27+d28+d29+d30+d31+d32+d33+d34+d35+d36+d37+d38+d39+d40+d41+d42+d43};
ListD listD=new ListD(di); 
list = list.add(listD) ;
b={{53+59}+{69+70}+{68+79+80+81}+{58+82+90+91+92+93}+{94+98+99+100+101+102}+{103+105+106+107+108+109+110}+{111+114+115+116+117+118+119}+{112+120+121+122+123+124+125}+{126+128+129+130+131+132+133}+{127+137}+{134+141+142}};
D d0=new D(52,b);
b={{52+59}+{69+70}+{68+79+80+81}+{58+82+90+91+92+93}+{94+98+99+100+101+102}+{103+105+106+107+108+109+110}+{111+114+115+116+117+118+119}+{112+120+121+122+123+124+125}+{126+128+129+130+131+132+133}+{127+137}+{134+141+142}};
D d1=new D(53,b);
b={{68}+{69+79}+{59+70+80+90+91}+{52+53+81+92+98+99+100}+{82+93+101+105+106+107+108}+{94+102+109+114+115+116+117}+{103+110+118+120+121+122+123}+{111+119+124+128+129+130+131+132}+{112+125+133+137}+{126+141+142}+{127+148}};
D d2=new D(58,b);
b={{52+53+69+70}+{68+79+80+81}+{58+82+90+91+92+93}+{94+98+99+100+101+102}+{103+105+106+107+108+109+110}+{111+114+115+116+117+118+119}+{112+120+121+122+123+124+125}+{126+128+129+130+131+132+133}+{127+137}+{134+141+142}+{138+148}};
D d3=new D(59,b);
b={{58+69+79}+{59+70+80+90+91}+{52+53+81+92+98+99+100}+{82+93+101+105+106+107+108}+{94+102+109+114+115+116+117}+{103+110+118+120+121+122+123}+{111+119+124+128+129+130+131+132}+{112+125+133+137}+{126+141+142}+{127+148}+{134}};
D d4=new D(68,b);
b={{59+68+70+79+80}+{52+53+58+81+90+91+92}+{82+93+98+99+100+101}+{94+102+105+106+107+108+109}+{103+110+114+115+116+117+118}+{111+119+120+121+122+123+124}+{112+125+128+129+130+131+132+133}+{126+137}+{127+141+142}+{134+148}+{138}};
D d5=new D(69,b);
b={{59+69+80+81}+{52+53+68+79+82+91+92+93}+{58+90+94+99+100+101+102}+{98+103+106+107+108+109+110}+{105+111+114+115+116+117+118+119}+{112+120+121+122+123+124+125}+{126+128+129+130+131+132+133}+{127+137}+{134+141+142}+{138+148}+{139+144}};
D d6=new D(70,b);
b={{68+69+80+90+91}+{58+59+70+81+92+98+99+100}+{52+53+82+93+101+105+106+107+108}+{94+102+109+114+115+116+117}+{103+110+118+120+121+122+123}+{111+119+124+128+129+130+131+132}+{112+125+133+137}+{126+141+142}+{127+148}+{134}+{138}};
D d7=new D(79,b);
b={{69+70+79+81+91+92}+{59+68+82+90+93+99+100+101}+{52+53+58+94+98+102+106+107+108+109}+{103+105+110+114+115+116+117+118}+{111+119+120+121+122+123+124}+{112+125+128+129+130+131+132+133}+{126+137}+{127+141+142}+{134+148}+{138}+{139+144}};
D d8=new D(80,b);
b={{70+80+82+92+93}+{59+69+79+91+94+100+101+102}+{52+53+68+90+99+103+107+108+109+110}+{58+98+106+111+115+116+117+118+119}+{105+112+114+120+121+122+123+124+125}+{126+128+129+130+131+132+133}+{127+137}+{134+141+142}+{138+148}+{139+144}+{135+140+145+151}};
D d9=new D(81,b);
b={{81+93+94}+{70+80+92+101+102+103}+{59+69+79+91+100+108+109+110+111}+{52+53+68+90+99+107+112+116+117+118+119}+{58+98+106+115+121+122+123+124+125}+{105+114+120+126+129+130+131+132+133}+{127+128+137}+{134+141+142}+{138+148}+{139+144}+{135+140+145+151}};
D d10=new D(82,b);
b={{79+91+98+99}+{68+69+80+92+100+105+106+107}+{58+59+70+81+93+101+108+114+115+116}+{52+53+82+94+102+109+117+120+121+122}+{103+110+118+123+128+129+130+131}+{111+119+124+132+137}+{112+125+133+141+142}+{126+148}+{127}+{134}+{138}};
D d11=new D(90,b);
b={{79+80+90+92+99+100}+{68+69+70+81+93+98+101+106+107+108}+{58+59+82+94+102+105+109+114+115+116+117}+{52+53+103+110+118+120+121+122+123}+{111+119+124+128+129+130+131+132}+{112+125+133+137}+{126+141+142}+{127+148}+{134}+{138}+{139+144}};
D d12=new D(91,b);
b={{80+81+91+93+100+101}+{69+70+79+82+90+94+99+102+107+108+109}+{59+68+98+103+106+110+115+116+117+118}+{52+53+58+105+111+114+119+120+121+122+123+124}+{112+125+128+129+130+131+132+133}+{126+137}+{127+141+142}+{134+148}+{138}+{139+144}+{135+140+145+151}};
D d13=new D(92,b);
b={{81+82+92+94+101+102}+{70+80+91+100+103+108+109+110}+{59+69+79+90+99+107+111+116+117+118+119}+{52+53+68+98+106+112+115+121+122+123+124+125}+{58+105+114+120+126+129+130+131+132+133}+{127+128+137}+{134+141+142}+{138+148}+{139+144}+{135+140+145+151}+{136+146}};
D d14=new D(93,b);
b={{82+93+102+103}+{81+92+101+109+110+111}+{70+80+91+100+108+112+117+118+119}+{59+69+79+90+99+107+116+122+123+124+125}+{52+53+68+98+106+115+121+126+130+131+132+133}+{58+105+114+120+127+129}+{128+134+137}+{138+141+142}+{139+144+148}+{135+140+145+151}+{136+146}};
D d15=new D(94,b);
b={{90+99+105+106}+{79+91+100+107+114+115}+{68+69+80+92+101+108+116+120+121}+{58+59+70+81+93+102+109+117+122+128+129+130}+{52+53+82+94+103+110+118+123+131+137}+{111+119+124+132+141+142}+{112+125+133+148}+{126}+{127}+{134}+{138}};
D d16=new D(98,b);
b={{90+91+98+100+106+107}+{79+80+92+101+105+108+114+115+116}+{68+69+70+81+93+102+109+117+120+121+122}+{58+59+82+94+103+110+118+123+128+129+130+131}+{52+53+111+119+124+132+137}+{112+125+133+141+142}+{126+148}+{127}+{134}+{138}+{139+144}};
D d17=new D(99,b);
b={{91+92+99+101+107+108}+{79+80+81+90+93+98+102+106+109+115+116+117}+{68+69+70+82+94+103+105+110+114+118+120+121+122+123}+{58+59+111+119+124+128+129+130+131+132}+{52+53+112+125+133+137}+{126+141+142}+{127+148}+{134}+{138}+{139+144}+{135+140+145+151}};
D d18=new D(100,b);
b={{92+93+100+102+108+109}+{80+81+82+91+94+99+103+107+110+116+117+118}+{69+70+79+90+98+106+111+115+119+121+122+123+124}+{59+68+105+112+114+120+125+129+130+131+132+133}+{52+53+58+126+128+137}+{127+141+142}+{134+148}+{138}+{139+144}+{135+140+145+151}+{136+146}};
D d19=new D(101,b);
b={{93+94+101+103+109+110}+{81+82+92+100+108+111+117+118+119}+{70+80+91+99+107+112+116+122+123+124+125}+{59+69+79+90+98+106+115+121+126+130+131+132+133}+{52+53+68+105+114+120+127+129}+{58+128+134+137}+{138+141+142}+{139+144+148}+{135+140+145+151}+{136+146}+{147}};
D d20=new D(102,b);
b={{94+102+110+111}+{82+93+101+109+112+118+119}+{81+92+100+108+117+123+124+125}+{70+80+91+99+107+116+122+126+131+132+133}+{59+69+79+90+98+106+115+121+127+130}+{52+53+68+105+114+120+129+134}+{58+128+137+138}+{139+141+142+144}+{135+140+145+148+151}+{136+146}+{147}};
D d21=new D(103,b);
b={{98+106+114}+{90+99+107+115+120}+{79+91+100+108+116+121+128+129}+{68+69+80+92+101+109+117+122+130+137}+{58+59+70+81+93+102+110+118+123+131+141+142}+{52+53+82+94+103+111+119+124+132+148}+{112+125+133}+{126}+{127}+{134}+{138}};
D d22=new D(105,b);
b={{98+99+105+107+114+115}+{90+91+100+108+116+120+121}+{79+80+92+101+109+117+122+128+129+130}+{68+69+70+81+93+102+110+118+123+131+137}+{58+59+82+94+103+111+119+124+132+141+142}+{52+53+112+125+133+148}+{126}+{127}+{134}+{138}+{139+144}};
D d23=new D(106,b);
b={{99+100+106+108+115+116}+{90+91+92+98+101+105+109+114+117+120+121+122}+{79+80+81+93+102+110+118+123+128+129+130+131}+{68+69+70+82+94+103+111+119+124+132+137}+{58+59+112+125+133+141+142}+{52+53+126+148}+{127}+{134}+{138}+{139+144}+{135+140+145+151}};
D d24=new D(107,b);
b={{100+101+107+109+116+117}+{91+92+93+99+102+106+110+115+118+121+122+123}+{79+80+81+82+90+94+98+103+105+111+114+119+120+124+129+130+131+132}+{68+69+70+112+125+128+133+137}+{58+59+126+141+142}+{52+53+127+148}+{134}+{138}+{139+144}+{135+140+145+151}+{136+146}};
D d25=new D(108,b);
b={{101+102+108+110+117+118}+{92+93+94+100+103+107+111+116+119+122+123+124}+{80+81+82+91+99+106+112+115+121+125+130+131+132+133}+{69+70+79+90+98+105+114+120+126+129}+{59+68+127+128+137}+{52+53+58+134+141+142}+{138+148}+{139+144}+{135+140+145+151}+{136+146}+{147}};
D d26=new D(109,b);
b={{102+103+109+111+118+119}+{93+94+101+108+112+117+123+124+125}+{81+82+92+100+107+116+122+126+131+132+133}+{70+80+91+99+106+115+121+127+130}+{59+69+79+90+98+105+114+120+129+134}+{52+53+68+128+137+138}+{58+139+141+142+144}+{135+140+145+148+151}+{136+146}+{147}+{152}};
D d27=new D(110,b);
b={{103+110+112+119}+{94+102+109+118+124+125}+{82+93+101+108+117+123+126+132+133}+{81+92+100+107+116+122+127+131}+{70+80+91+99+106+115+121+130+134}+{59+69+79+90+98+105+114+120+129+138}+{52+53+68+128+137+139+144}+{58+135+140+141+142+145+151}+{136+146+148}+{147}+{152}};
D d28=new D(111,b);
b={{111}+{103+110+119}+{94+102+109+118+124+125}+{82+93+101+108+117+123+126+132+133}+{81+92+100+107+116+122+127+131}+{70+80+91+99+106+115+121+130+134}+{59+69+79+90+98+105+114+120+129+138}+{52+53+68+128+137+139+144}+{58+135+140+141+142+145+151}+{136+146+148}+{147}};
D d29=new D(112,b);
b={{105+106+115+120}+{98+99+107+116+121+128+129}+{90+91+100+108+117+122+130+137}+{79+80+92+101+109+118+123+131+141+142}+{68+69+70+81+93+102+110+119+124+132+148}+{58+59+82+94+103+111+125+133}+{52+53+112+126}+{127}+{134}+{138}+{139+144}};
D d30=new D(114,b);
b={{106+107+114+116+120+121}+{98+99+100+105+108+117+122+128+129+130}+{90+91+92+101+109+118+123+131+137}+{79+80+81+93+102+110+119+124+132+141+142}+{68+69+70+82+94+103+111+125+133+148}+{58+59+112+126}+{52+53+127}+{134}+{138}+{139+144}+{135+140+145+151}};
D d31=new D(115,b);
b={{107+108+115+117+121+122}+{99+100+101+106+109+114+118+120+123+129+130+131}+{90+91+92+93+98+102+105+110+119+124+128+132+137}+{79+80+81+82+94+103+111+125+133+141+142}+{68+69+70+112+126+148}+{58+59+127}+{52+53+134}+{138}+{139+144}+{135+140+145+151}+{136+146}};
D d32=new D(116,b);
b={{108+109+116+118+122+123}+{100+101+102+107+110+115+119+121+124+130+131+132}+{91+92+93+94+99+103+106+111+114+120+125+129+133}+{79+80+81+82+90+98+105+112+126+128+137}+{68+69+70+127+141+142}+{58+59+134+148}+{52+53+138}+{139+144}+{135+140+145+151}+{136+146}+{147}};
D d33=new D(117,b);
b={{109+110+117+119+123+124}+{101+102+103+108+111+116+122+125+131+132+133}+{92+93+94+100+107+112+115+121+126+130}+{80+81+82+91+99+106+114+120+127+129}+{69+70+79+90+98+105+128+134+137}+{59+68+138+141+142}+{52+53+58+139+144+148}+{135+140+145+151}+{136+146}+{147}+{152}};
D d34=new D(118,b);
b={{110+111+118+124+125}+{102+103+109+112+117+123+126+132+133}+{93+94+101+108+116+122+127+131}+{81+82+92+100+107+115+121+130+134}+{70+80+91+99+106+114+120+129+138}+{59+69+79+90+98+105+128+137+139+144}+{52+53+68+135+140+141+142+145+151}+{58+136+146+148}+{147}+{152}+{153}};
D d35=new D(119,b);
b={{114+115+121+128+129}+{105+106+107+116+122+130+137}+{98+99+100+108+117+123+131+141+142}+{90+91+92+101+109+118+124+132+148}+{79+80+81+93+102+110+119+125+133}+{68+69+70+82+94+103+111+126}+{58+59+112+127}+{52+53+134}+{138}+{139+144}+{135+140+145+151}};
D d36=new D(120,b);
b={{115+116+120+122+129+130}+{106+107+108+114+117+123+128+131+137}+{98+99+100+101+105+109+118+124+132+141+142}+{90+91+92+93+102+110+119+125+133+148}+{79+80+81+82+94+103+111+126}+{68+69+70+112+127}+{58+59+134}+{52+53+138}+{139+144}+{135+140+145+151}+{136+146}};
D d37=new D(121,b);
b={{116+117+121+123+130+131}+{107+108+109+115+118+120+124+129+132}+{99+100+101+102+106+110+114+119+125+128+133+137}+{90+91+92+93+94+98+103+105+111+126+141+142}+{79+80+81+82+112+127+148}+{68+69+70+134}+{58+59+138}+{52+53+139+144}+{135+140+145+151}+{136+146}+{147}};
D d38=new D(122,b);
b={{117+118+122+124+131+132}+{108+109+110+116+119+121+125+130+133}+{100+101+102+103+107+111+115+120+126+129}+{91+92+93+94+99+106+112+114+127+128+137}+{79+80+81+82+90+98+105+134+141+142}+{68+69+70+138+148}+{58+59+139+144}+{52+53+135+140+145+151}+{136+146}+{147}+{152}};
D d39=new D(123,b);
b={{118+119+123+125+132+133}+{109+110+111+117+122+126+131}+{101+102+103+108+112+116+121+127+130}+{92+93+94+100+107+115+120+129+134}+{80+81+82+91+99+106+114+128+137+138}+{69+70+79+90+98+105+139+141+142+144}+{59+68+135+140+145+148+151}+{52+53+58+136+146}+{147}+{152}+{153}};
D d40=new D(124,b);
b={{119+124+126+133}+{110+111+118+123+127+132}+{102+103+109+112+117+122+131+134}+{93+94+101+108+116+121+130+138}+{81+82+92+100+107+115+120+129+139+144}+{70+80+91+99+106+114+128+135+137+140+145+151}+{59+69+79+90+98+105+136+141+142+146}+{52+53+68+147+148}+{58+152}+{153}};
D d41=new D(125,b);
b={{125+127}+{119+124+133+134}+{110+111+118+123+132+138}+{102+103+109+112+117+122+131+139+144}+{93+94+101+108+116+121+130+135+140+145+151}+{81+82+92+100+107+115+120+129+136+146}+{70+80+91+99+106+114+128+137+147}+{59+69+79+90+98+105+141+142+152}+{52+53+68+148+153}+{58}};
D d42=new D(126,b);
b={{126+134}+{125+138}+{119+124+133+139+144}+{110+111+118+123+132+135+140+145+151}+{102+103+109+112+117+122+131+136+146}+{93+94+101+108+116+121+130+147}+{81+82+92+100+107+115+120+129+152}+{70+80+91+99+106+114+128+137+153}+{59+69+79+90+98+105+141+142}+{52+53+68+148}+{58}};
D d43=new D(127,b);
b={{120+129+137}+{114+115+121+130+141+142}+{105+106+107+116+122+131+148}+{98+99+100+108+117+123+132}+{90+91+92+101+109+118+124+133}+{79+80+81+93+102+110+119+125}+{68+69+70+82+94+103+111+126}+{58+59+112+127}+{52+53+134}+{138}+{139+144}};
D d44=new D(128,b);
b={{120+121+128+130+137}+{114+115+116+122+131+141+142}+{105+106+107+108+117+123+132+148}+{98+99+100+101+109+118+124+133}+{90+91+92+93+102+110+119+125}+{79+80+81+82+94+103+111+126}+{68+69+70+112+127}+{58+59+134}+{52+53+138}+{139+144}+{135+140+145+151}};
D d45=new D(129,b);
b={{121+122+129+131}+{115+116+117+120+123+128+132+137}+{106+107+108+109+114+118+124+133+141+142}+{98+99+100+101+102+105+110+119+125+148}+{90+91+92+93+94+103+111+126}+{79+80+81+82+112+127}+{68+69+70+134}+{58+59+138}+{52+53+139+144}+{135+140+145+151}+{136+146}};
D d46=new D(130,b);
b={{122+123+130+132}+{116+117+118+121+124+129+133}+{107+108+109+110+115+119+120+125+128+137}+{99+100+101+102+103+106+111+114+126+141+142}+{90+91+92+93+94+98+105+112+127+148}+{79+80+81+82+134}+{68+69+70+138}+{58+59+139+144}+{52+53+135+140+145+151}+{136+146}+{147}};
D d47=new D(131,b);
b={{123+124+131+133}+{117+118+119+122+125+130}+{108+109+110+111+116+121+126+129}+{100+101+102+103+107+112+115+120+127+128+137}+{91+92+93+94+99+106+114+134+141+142}+{79+80+81+82+90+98+105+138+148}+{68+69+70+139+144}+{58+59+135+140+145+151}+{52+53+136+146}+{147}+{152}};
D d48=new D(132,b);
b={{124+125+132}+{118+119+123+126+131}+{109+110+111+117+122+127+130}+{101+102+103+108+112+116+121+129+134}+{92+93+94+100+107+115+120+128+137+138}+{80+81+82+91+99+106+114+139+141+142+144}+{69+70+79+90+98+105+135+140+145+148+151}+{59+68+136+146}+{52+53+58+147}+{152}+{153}};
D d49=new D(133,b);
b={{127+138}+{126+139+144}+{125+135+140+145+151}+{119+124+133+136+146}+{110+111+118+123+132+147}+{102+103+109+112+117+122+131+152}+{93+94+101+108+116+121+130+153}+{81+82+92+100+107+115+120+129}+{70+80+91+99+106+114+128+137}+{59+69+79+90+98+105+141+142}+{52+53+68+148}};
D d50=new D(134,b);
b={{136+139+140}+{138+145+146}+{134+144+147}+{127+151+152}+{126+153}+{125}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}};
D d51=new D(135,b);
b={{135+140}+{139+145+146}+{138+147}+{134+144+152}+{127+151+153}+{126}+{125}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}};
D d52=new D(136,b);
b={{128+129+141+142}+{120+121+130+148}+{114+115+116+122+131}+{105+106+107+108+117+123+132}+{98+99+100+101+109+118+124+133}+{90+91+92+93+102+110+119+125}+{79+80+81+82+94+103+111+126}+{68+69+70+112+127}+{58+59+134}+{52+53+138}+{139+144}};
D d53=new D(137,b);
b={{134+139+144}+{127+135+140+145+151}+{126+136+146}+{125+147}+{119+124+133+152}+{110+111+118+123+132+153}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}+{70+80+91+99+106+114+128+137}+{59+69+79+90+98+105+141+142}};
D d54=new D(138,b);
b={{135+138+140+145}+{134+136+144+146}+{127+147+151}+{126+152}+{125+153}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}+{70+80+91+99+106+114+128+137}};
D d55=new D(139,b);
b={{135+136+139+145+146}+{138+147}+{134+144+152}+{127+151+153}+{126}+{125}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}};
D d56=new D(140,b);
b={{137+142+148}+{128+129}+{120+121+130}+{114+115+116+122+131}+{105+106+107+108+117+123+132}+{98+99+100+101+109+118+124+133}+{90+91+92+93+102+110+119+125}+{79+80+81+82+94+103+111+126}+{68+69+70+112+127}+{58+59+134}+{52+53+138}};
D d57=new D(141,b);
b={{137+141+148}+{128+129}+{120+121+130}+{114+115+116+122+131}+{105+106+107+108+117+123+132}+{98+99+100+101+109+118+124+133}+{90+91+92+93+102+110+119+125}+{79+80+81+82+94+103+111+126}+{68+69+70+112+127}+{58+59+134}+{52+53+138}};
D d58=new D(142,b);
b={{138+151}+{134+139}+{127+135+140+145}+{126+136+146}+{125+147}+{119+124+133+152}+{110+111+118+123+132+153}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}+{70+80+91+99+106+114+128+137}};
D d59=new D(144,b);
b={{139+140+146}+{135+136+138+147}+{134+144+152}+{127+151+153}+{126}+{125}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}};
D d60=new D(145,b);
b={{140+145+147}+{135+136+139+152}+{138+153}+{134+144}+{127+151}+{126}+{125}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}};
D d61=new D(146,b);
b={{146+152}+{140+145+153}+{135+136+139}+{138}+{134+144}+{127+151}+{126}+{125}+{119+124+133}+{110+111+118+123+132}+{102+103+109+112+117+122+131}};
D d62=new D(147,b);
b={{141+142}+{137}+{128+129}+{120+121+130}+{114+115+116+122+131}+{105+106+107+108+117+123+132}+{98+99+100+101+109+118+124+133}+{90+91+92+93+102+110+119+125}+{79+80+81+82+94+103+111+126}+{68+69+70+112+127}+{58+59+134}};
D d63=new D(148,b);
b={{144}+{138}+{134+139}+{127+135+140+145}+{126+136+146}+{125+147}+{119+124+133+152}+{110+111+118+123+132+153}+{102+103+109+112+117+122+131}+{93+94+101+108+116+121+130}+{81+82+92+100+107+115+120+129}};
D d64=new D(151,b);
b={{147+153}+{146}+{140+145}+{135+136+139}+{138}+{134+144}+{127+151}+{126}+{125}+{119+124+133}+{110+111+118+123+132}};
D d65=new D(152,b);
b={{152}+{147}+{146}+{140+145}+{135+136+139}+{138}+{134+144}+{127+151}+{126}+{125}+{119+124+133}};
D d66=new D(153,b);
D[] di={d0+d1+d2+d3+d4+d5+d6+d7+d8+d9+d10+d11+d12+d13+d14+d15+d16+d17+d18+d19+d20+d21+d22+d23+d24+d25+d26+d27+d28+d29+d30+d31+d32+d33+d34+d35+d36+d37+d38+d39+d40+d41+d42+d43+d44+d45+d46+d47+d48+d49+d50+d51+d52+d53+d54+d55+d56+d57+d58+d59+d60+d61+d62+d63+d64+d65+d66};
ListD listD=new ListD(di); 
list = list.add(listD) ;
