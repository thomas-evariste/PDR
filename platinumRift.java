import java.util.*;
import java.io.*;
import java.math.*;


class Player {

    public static void main(String args[]) {

/////////////////////// INITIALISATION ////////////////////////////////////////////
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
            cellulesNonConquises.add(zoneId);
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
        
        // On trie les voisins de chaque cellule du plus au moins chargé en platinum
        for(i = 0 ; i < pangee.size() ; i++){ 
            pangee.triVoisinDe(i);
        }
        
        
        Graphe graphe = Tools.splitContinent( pangee ); 
		
		for(Continent continent : graphe.getContinents()){
			continent.calculDensitePlatinum();
		}
        
        

        
////////////////  PROCEDURE A CHAQUE TOUR /////////////////////////////////////////
     
        int zID; //L'ID de la cellule qu'on manipulera 
		int myPlatinum;
		String deplacementStr;
		Cellule cellule;
        List<Integer> deplacement = new ArrayList<Integer>();
		int arrivee;
		int voisin;
		int nbMaxCree;
		String placement;
		Continent continent;
        while (true) {
            
            ////// PHASE DE RECUPERATION DE DONNEES //////
           
            cellulesNonConquises.clear();
            myPlatinum = in.nextInt(); // Mon platinum
            for (i = 0; i < zoneCount; i++) {
                
                zID = in.nextInt(); // On récupère l'ID de la cellule
                graphe.getCelluleById(i).setControl(in.nextInt());
                if(graphe.getCelluleById(i).getControl()==-1){
                    cellulesNonConquises.add(i);
                }
                for(j=0; j<4; j++){
                    graphe.getCelluleById(i).setRobots(in.nextInt(), j);
                }
            }
            
            // Si il reste des cellules non conquises on met le graphe des cellules non conquises à jour
         //   if(!celluleNonConquises.isEmpty()){
     //           Tools.miseAJourNonConquis(grapheNonConquis, graphe); 
     //       }
            //Tools.miseAJourGraphe(listInfosTempsReel, graphe); //A corriger attention listInfosTempsReel n'existe plus
            
            
            
            
            ////////// PHASE DE DEPLACEMENT ////////////

            deplacementStr = "";


            for(j=0;j<graphe.size();j++){
				continent=graphe.getContinent(j);
                for(k=0;k<continent.size();k++){
                    cellule = continent.getCellule(k);
                    // Si on a au moins un robot disponible sur la cellule
                    if( cellule.getRobots(myId) != 0 ){
                        deplacement.clear(); 
                        // Pour chaque robot
                        for(i = 0 ; i < cellule.getRobots(myId) ; i++){
                        
                            arrivee = Tools.destination(cellule.getId(),graphe); //On récupère un voisin aléatoirement
                            // Pour chaque voisin de notre cellule
                            for (l=0;l<cellule.nbVoisins();l++){
								voisin = cellule.getVoisin(l);
                                // Si nous ne sommes pas propriétaire de la meilleure cellule voisine et qu'un autre robot venant de la cellule de départ n'a pas déjà fait ce déplacement on change l'arrivée
                                if (( graphe.getCelluleById(voisin).getControl()!=myId) && (!deplacement.contains(voisin))){
                                    arrivee = voisin;
                                    deplacement.add(arrivee);
                                    break;
                                }
                            }
                        
                           deplacementStr = deplacementStr + " " + 1 + " " + cellule.getId() + " " + arrivee; //On déplace un robot à la fois
                            
                        }
                    }
                }
            }

            if(deplacementStr==""){deplacementStr="WAIT";}
            System.out.println(deplacementStr);
            
            
            ///////////  PHASE D'ACHAT ET PLACEMENT DE NOUVEAUX ROBOTS ///////////////
            
            nbMaxCree = myPlatinum / 20;
            placement = "";
            for(i=0;i<nbMaxCree;i++){
                // Si toutes les cellules sont possédées on se place aléatoirement sur l'une d'elles
                if(cellulesNonConquises.isEmpty()){
                    placement = placement + " 1 " + String.valueOf(Tools.positionAlea(graphe)) ;
                } 
                // Si il reste des cellules non possédées on se place aléatoirement sur l'une d'elles
                else {
                    placement = placement + " 1 " + String.valueOf(Tools.takeRandom(cellulesNonConquises)) ;
                }
            }
            
            if(placement==""){placement="WAIT";}
            
            System.out.println(placement);
        }
    }
    
    
        
}


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////         TOOLS         /////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////



class Tools{

    static List<Integer> jap;
    static List<Integer> ameriqueDuN;
    static List<Integer> ameriqueDuS;
    static List<Integer> antarct;
    
    
    
    static int destination(int idCellule, Graphe graphe){ // Déplacement sur un voisin au hasard
        Random rand = new Random();
        int arrivee = graphe.getCelluleById(idCellule).getVoisin(rand.nextInt(graphe.getCelluleById(idCellule).nbVoisins()));
        return arrivee;
    }
    
    static int positionAlea(Graphe graphe){  //On récupère une cellule au hasard sur le graphe
        Random rand = new Random();
        int pos = graphe.getCellule(rand.nextInt(graphe.sizeCellule())).getId();       
        return pos;
    }
    
    //static int[] classementCellulesDepart(Graphe graphe){}
        
    
    
    
    static Graphe splitContinent( Continent continent ) {  //Initialisation des continents
        Graphe graphe = new Graphe();
        Continent ameriqueDuNord = new Continent(0);
        Continent ameriqueDuSudAfrique = new Continent(1);
        Continent antarctique = new Continent(2);
        Continent europeAsieOceanie = new Continent(3);
        Continent japon = new Continent(4);
        Cellule cellule;
		int i;
        for (i=0;i<continent.size();i++){
			cellule=continent.getCellule(i);
            int id = cellule.getId();
            
            if (ameriqueDuS.contains(id)){
                ameriqueDuSudAfrique.addCellule(cellule);
            }
            else if(ameriqueDuN.contains(id)){
                ameriqueDuNord.addCellule(cellule);
            }
            else if(antarct.contains(id)){
                antarctique.addCellule(cellule);
            }
            else if(jap.contains(id)){
                japon.addCellule(cellule);
            }
            else{
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
    
    static void remplirListIDContinent(){ //Suite de l'initialisation des continents
        Integer[] j = new Integer[] { 143,149, 150 }; 
        Integer[] an = new Integer[] { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,19,20,21,22,23,27,28,29,37,38,43,46,47,48,49 }; 
        Integer[] as = new Integer[] { 18,24,25,26,30,31,32,33,34,35,36,39,40,41,42,44,45,50,51,54,55,56,60,61,62,63,64,65,66,71,72,73,74,75,76,77,83,84,85,86,87,88,95,96 }; 
        Integer[] ant = new Integer[] { 57,67,78,89,97,104,113 }; 
        jap = Arrays.asList(j);
        ameriqueDuN = Arrays.asList(an);
        ameriqueDuS = Arrays.asList(as);
        antarct = Arrays.asList(ant);
        
    }
        
 /*   static void miseAJourNonConquis(ArrayList<int> cellulesNonConquises, Graphe graphe){ 
        ArrayList<Integer> supp = new ArrayList<Integer>();
        for(Continent continent : grapheNonConquis.getContinents()){
            for(Cellule cellule : continent.getCellules()){
                if((graphe.getCellule(cellule.getId()).getControl())!=-1){
                    supp.add(cellule.getId());
                }
            }
        }
        for(int id : supp){
            grapheNonConquis.removeCelluleById(id);
        }
    }*/
    
/*    static void miseAJourPossessions(ArrayList<int[]> listInfosTempsReel,Graphe graphe){           //Plus adapté à la nouvelle modélisation
        for(int i = 0; i < graphe.sizeCellule();i++ ){
            graphe.getCellule(i).setControl(listInfosTempsReel.get(graphe.getCellule(i).getId())[1]);
        }
    }*/
    
    static void miseAJourContinent(Graphe graphe){
        ArrayList<Integer> supp = new ArrayList<Integer>();
		Continent continent;
		int j;
        for(j=0;j<graphe.size();j++){
			continent=graphe.getContinent(j);
            if(continent.verifPoss()){
                supp.add(continent.getId());
            }
        }
		int s;
		for(j=0;j<supp.size();j++){
			s=supp.get(j);
            graphe.removeContinentById(s);
        }
    }
    
    static void miseAJourGraphe(Graphe graphe){
        miseAJourContinent(graphe);
    }
    
    static int takeRandom(ArrayList<Integer> liste){
        int taille = liste.size();
        Random rand = new Random();
        int hasard = rand.nextInt(taille);
        return liste.get(hasard);
    }
    
    
}


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////         CELLULE       ////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////


class Cellule{

    private int id;
    private int controlePar;
    private int[] robots;
    private int platinum;
    private ArrayList<Integer> voisins;


    Cellule(int id, int platinum, ArrayList<Integer> voisin){
        this.id = id;
        this.platinum = platinum;
        this.voisins = voisins;
        controlePar=-1;
        robots = new int[4];
		int i;
        for(i=0;i<4;i++){
            robots[i]=0;
        }
    }
    
    Cellule(int id, int platinum){
        this.id = id;
        this.platinum = platinum;
        voisins = new ArrayList<Integer>();
        controlePar=-1;
        robots = new int[4];
		int i;
        for(i=0;i<4;i++){
            robots[i]=0;
        }
        
    }
    
    Cellule(){
        id = -1;
        platinum = -1;
        voisins = new ArrayList<Integer>();
        controlePar=-1;
        robots = new int[4];
		int i;
        for(i=0;i<4;i++){
            robots[i]=0;
        }
    }

    int getId(){
        return id;
    }

    int getControl(){
        return controlePar;
    }

    int getPlatinum(){
        return platinum;
    }

    ArrayList<Integer> getVoisins(){
        return voisins;
    }

    int getVoisin(int num){
        return voisins.get(num);
    }
    
    int getRobots(int i){
        return robots[i];
    }
    
    Boolean verifVoisin(int id){
        return voisins.contains(id);
    }
    
    void setId(int id){
        this.id = id;    
    }

    void setControl(int numJ){
        controlePar = numJ;
    }
    
    void setPlatinum(int platinum){
        this.platinum = platinum;    
    }
    
    void setVoisins(ArrayList<Integer> voisins){
        this.voisins = voisins;    
    }
    
    void setRobots(int nombre, int i){
        this.robots[i]= nombre;
    }
    
    void addVoisin(int idVoisin){
        voisins.add(idVoisin);    
    }
    
    void addVoisinA(int idVoisin, int pos){
        voisins.add(pos,idVoisin);    
    }
    
    void suppVoisin(int pos){
        voisins.remove(pos);    
    }
    
    void videVoisin(){
        voisins.clear();    
    }
    
    int nbVoisins(){
        return voisins.size();
    }
 
}


///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////        GRAPHE        /////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

class Graphe {
    private ArrayList<Continent> continents;
    
    Graphe(){
        continents = new ArrayList<Continent>();
    }
    
    Graphe(Graphe graphe){
        continents = new ArrayList<Continent>();
		Continent continent;
		int j;
        for(j=0;j<graphe.size();j++){
			continent=graphe.getContinent(j);
            continents.add(new Continent(continent));
        }
    }
    
    ArrayList<Continent> getContinents(){
        return continents;
    }
    
    Continent getContinent(int num){
        return continents.get(num);
    }
    
    Continent getContinentById(int id){
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
    
    
    void setContinents(ArrayList<Continent> continents){
        this.continents = new ArrayList<Continent>(continents);
    }
    
    void addContinent(Continent continent){
        continents.add(continent);
    }
    
    void addContinents(ArrayList<Continent> continents){
		Continent continent;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
            this.addContinent(continent);
        }
    }
    
    void removeContinent(int num){
        continents.remove(num);
    }
    
    void removeContinentById(int id){
		int i;
        for(i = 0; i< continents.size();i++){
            if(continents.get(i).getId()==id){
                continents.remove(i);
                i--;
            }
        }
    }
    
    int size(){
        return continents.size();
    }
    
    int sizeCellule(){
        int taille = 0;
		Continent continent;
		int j;
        for(j=0;j<continents.size();j++){
			continent=continents.get(j);
            taille += continent.size();
        }
        return taille;
    }
    
    Boolean isEmptyContinent(){
        return continents.isEmpty();
    }
    
    Boolean isEmptyCase(){
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
    
    Cellule getCelluleById(int id){
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
    
    Cellule getCellule(int num){
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
    
    void removeCelluleById(int id){
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


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////       CONTINENT       /////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

class Continent {
    private ArrayList<Cellule> cellules;
    private int id;
	private double densitePlatinum;
    
    Continent(){
        cellules = new ArrayList<Cellule>();
        id=-1;
		densitePlatinum =0;
    }
    
    Continent(int id){
        cellules = new ArrayList<Cellule>();
        this.id=id;
		densitePlatinum =0;
    }
    
    Continent(ArrayList<Cellule> cellules){
        this.cellules = cellules;
        id=-1;
		densitePlatinum =0;
    }
    
    Continent(ArrayList<Cellule> cellules, int id){
        this.cellules = cellules;
        this.id=id;
		densitePlatinum =0;
    }
    
    Continent(Continent continent){
        cellules = new ArrayList<Cellule>(continent.getCellules());
        id=continent.getId();
		densitePlatinum =0;
    }
    
    ArrayList<Cellule> getCellules(){
        return cellules;
    }
    
    Cellule getCellule(int num){
        return cellules.get(num);
    }
	
	double getDensitePlatinum(){
		return densitePlatinum;
	}
    
    Cellule getCelluleById(int id){
        Cellule celluleVide = new Cellule();
		Cellule cellule;
		int j;
        for(j=0;j<cellules.size();j++){
			cellule=cellules.get(j);
            if(id==cellule.getId()){
                return cellule;
            }
        }
        System.err.println("cellule pas dans le graphe: " + id);
        return celluleVide;
    }
    
    void setCellules(ArrayList<Cellule> cellules){
        this.cellules = cellules;
    }
	
	void setDensitePlatinum(double d){
		densitePlatinum=d;
	}
    
    void addCellule(Cellule cellule){
        cellules.add(cellule);
    }
    
    void addCellules(ArrayList<Cellule> cellules){
		Cellule cellule;
		int j;
        for(j=0;j<cellules.size();j++){
			cellule=cellules.get(j);
            this.addCellule(cellule);
        }
    }
    
    void removeCellule(int num){
        cellules.remove(num);
    }
    
    void removeCelluleById(int id){
		int i;
        for(i = 0; i< cellules.size();i++){
            if(cellules.get(i).getId()==id){
                cellules.remove(i);
                i--;
            }
        }
    }
    
    int size(){
        return cellules.size();
    }
    
    Boolean isEmpty(){
        return cellules.isEmpty();
    }
    
    void triVoisinDe(int id){ // Classe les voisins du plus chargé en platinum au moins chargé en platinum  // Pas opti à changer en cas de limite de temps
        ArrayList<Integer> voisin = this.getCelluleById(id).getVoisins();
        ArrayList<Integer> voisinTrie = new ArrayList<Integer>();
		int i;
		int j;
        for(i = 0; i<7;i++){
            for(j = 0 ; j<voisin.size();j++){
                if(this.getCelluleById(voisin.get(j)).getPlatinum()==(6-i)){
                    voisinTrie.add(voisin.get(j));
                }
            }
        }
        this.getCelluleById(id).setVoisins(voisinTrie);
    }
    
    void setId(int id){
        this.id =id;
    }
    
    int getId(){
        return id;
    }
    
    Boolean verifPoss(){
		int i;
        for(i =0; i< cellules.size()-1;i++){
            if(cellules.get(i).getControl()!=cellules.get(i+1).getControl()){
                return false;
            }
        }
        if(cellules.get(0).getControl()==-1){
            return false;
        }
        return true;
    }
    
	void calculDensitePlatinum(){
		double comptPaltinum=0;
		Cellule cellule;
		int j;
        for(j=0;j<cellules.size();j++){
			cellule=cellules.get(j);
			comptPaltinum += cellule.getPlatinum();
		}
		
		double densitePlatinum = comptPaltinum / ( (double) cellules.size());
		this.densitePlatinum = densitePlatinum;
		
	}
}
