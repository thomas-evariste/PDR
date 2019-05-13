package Model;

import java.util.*;

public class Player {

    public static void main(String args[]) {

/////////////////////// INITIALISATION ////////////////////////////////////////////
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
        
        // On trie les voisins de chaque cellule du plus au moins charg� en platinum
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
                
                zID = in.nextInt(); // On r�cup�re l'ID de la cellule
                graphe.getCelluleById(i).setControl(in.nextInt());
                if(graphe.getCelluleById(i).getControl()==-1){
                    cellulesNonConquises.add(i);
                }
                for(j=0; j<4; j++){
                    graphe.getCelluleById(i).setRobots(in.nextInt(), j);
                }
            }
            
            // Si il reste des cellules non conquises on met le graphe des cellules non conquises � jour
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
                        
                            arrivee = Tools.destination(cellule.getId(),graphe); //On r�cup�re un voisin al�atoirement
                            // Pour chaque voisin de notre cellule
                            for (l=0;l<cellule.nbVoisins();l++){
								voisin = cellule.getVoisin(l);
                                // Si nous ne sommes pas propri�taire de la meilleure cellule voisine et qu'un autre robot venant de la cellule de d�part n'a pas d�j� fait ce d�placement on change l'arriv�e
                                if (( graphe.getCelluleById(voisin).getControl()!=myId) && (!deplacement.contains(voisin))){
                                    arrivee = voisin;
                                    deplacement.add(arrivee);
                                    break;
                                }
                            }
                        
                           deplacementStr = deplacementStr + " " + 1 + " " + cellule.getId() + " " + arrivee; //On d�place un robot � la fois
                            
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
                // Si toutes les cellules sont poss�d�es on se place al�atoirement sur l'une d'elles
                if(cellulesNonConquises.isEmpty()){
                    placement = placement + " 1 " + String.valueOf(Tools.positionAlea(graphe)) ;
                } 
                // Si il reste des cellules non poss�d�es on se place al�atoirement sur l'une d'elles
                else {
                    placement = placement + " 1 " + String.valueOf(Tools.takeRandom(cellulesNonConquises)) ;
                }
            }
            
            if(placement==""){placement="WAIT";}
            
            System.out.println(placement);
        }
    }
    
    
        
}

