package Model;

import java.util.*;

public class Tools{

    static List<Integer> jap;
    static List<Integer> ameriqueDuN;
    static List<Integer> ameriqueDuS;
    static List<Integer> antarct;
    
    
    
    public static int destination(int idCellule, Graphe graphe){ // Déplacement sur un voisin au hasard
        Random rand = new Random();
        int arrivee = graphe.getCelluleById(idCellule).getVoisin(rand.nextInt(graphe.getCelluleById(idCellule).nbVoisins()));
        return arrivee;
    }
    
    public static int positionAlea(Graphe graphe){  //On récupère une cellule au hasard sur le graphe
        Random rand = new Random();
        int pos = graphe.getCellule(rand.nextInt(graphe.sizeCellule())).getId();       
        return pos;
    }
    
    //static int[] classementCellulesDepart(Graphe graphe){}
        
    
    
    
    public static Graphe splitContinent( Continent continent ) {  //Initialisation des continents
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
    
    public static void remplirListIDContinent(){ //Suite de l'initialisation des continents
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
    
    public static void miseAJourContinent(Graphe graphe){
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
    
    public static void miseAJourGraphe(Graphe graphe){
        miseAJourContinent(graphe);
    }
    
    public static int takeRandom(ArrayList<Integer> liste){
        int taille = liste.size();
        Random rand = new Random();
        int hasard = rand.nextInt(taille);
        return liste.get(hasard);
    }
    
    
}
