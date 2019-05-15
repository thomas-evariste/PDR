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
		int playerCount = in.nextInt(); // Nombre de joueurs (2 � 4)
		int myId = in.nextInt(); // Notre ID
		int zoneCount = in.nextInt(); // Nombre de cellules
		int linkCount = in.nextInt(); // Nombre de fronti�res
		int zoneId;
		int platinumSource;
		Continent pangee = new Continent();

		// Cr�ation des cellules et ajout � la pang�e
		for (i = 0; i < zoneCount; i++) {
			zoneId = in.nextInt(); // On r�cup�re un ID de cellule
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

		// On trie les voisins de chaque cellule du plus au moins charg� en
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

				zID = in.nextInt(); // On r�cup�re l'ID de la cellule
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
																					// r�cup�re
																					// un
																					// voisin
																					// al�atoirement
							// Pour chaque voisin de notre cellule
							for (l = 0; l < cellule.nbVoisins(); l++) {
								voisin = cellule.getVoisin(l);
								// Si nous ne sommes pas propri�taire de la
								// meilleure cellule voisine et qu'un autre
								// robot venant de la cellule de d�part n'a pas
								// d�j� fait ce d�placement on change l'arriv�e
								if ((graphe.getCelluleById(voisin).getControl() != myId)
										&& (!deplacement.contains(voisin))) {
									arrivee = voisin;
									deplacement.add(arrivee);
									break;
								}
							}

							deplacementStr = deplacementStr + " " + 1 + " " + cellule.getId() + " " + arrivee; // On
																												// d�place
																												// un
																												// robot
																												// �
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


class Tools {

	static List<Integer> jap;
	static List<Integer> ameriqueDuN;
	static List<Integer> ameriqueDuS;
	static List<Integer> antarct;

	public static int destination(int idCellule, Graphe graphe) { // D�placement sur un voisin au hasard
		Random rand = new Random();
		int arrivee = graphe.getCelluleById(idCellule)
				.getVoisin(rand.nextInt(graphe.getCelluleById(idCellule).nbVoisins()));
		return arrivee;
	}

	public static int positionAlea(Graphe graphe) { // On r�cup�re une cellule au hasard sur le graphe
		Random rand = new Random();
		int pos = graphe.getCellule(rand.nextInt(graphe.sizeCellule())).getId();
		return pos;
	}

	// static int[] classementCellulesDepart(Graphe graphe){}

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
	 * graphe){ //Plus adapt� � la nouvelle mod�lisation for(int i = 0; i <
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
																								// prorit� celles qui
																								// ont le plus de
																								// platinum (liste
																								// tri�e)
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

	public static int positionAleaV2(Graphe graphe, int myId, Boolean bool) { // On r�cup�re une cellule au hasard sur
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

		Random rand = new Random();
		int id = mesCellules.get(rand.nextInt(mesCellules.size()));
		return id;
	}

	// Pour n i�me plus grande mine non conquise (n = parcourtNonConquis) on trouve
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

			// Si toutes les cellules sont poss�d�es on se place al�atoirement sur l'une
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

	// Permet de cr�er la liste des cellules non conquises du meilleur continent au
	// moins bon, cela nous permettra d'avoir les cellules class�es par platinum
	// puis par densit� du continent
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

	public void triVoisinDe(int id) { // Classe les voisins du plus charg� en platinum
		// au moins charg� en platinum // Pas opti �
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


class Cellule {

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
