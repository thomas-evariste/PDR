package Parse;
import java.io.*;

import Model.*;

public class ParseTexteToGraphe {

	public static Graphe parse() throws IOException {
		InputStream flux;
		Graphe graphe =new Graphe();
		try {
			flux = new FileInputStream("C:/DATA/ISIC/PDR/PDR/donneesGraphe.txt");
		
		InputStreamReader lecture=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(lecture);
		String ligne;
		Boolean newContinent = false;
		int comptContinent =-1;
		Boolean newCellule = false;
		int comptCellule =-1;
		while ((ligne=buff.readLine())!=null){
			System.out.println(ligne);
			if(ligne.equals("continent")){
				Continent continent = new Continent();
				newContinent=true;
				graphe.addContinent(continent);
				comptContinent++;
				comptCellule =-1;
			}
			else if(newContinent){
				graphe.getContinent(comptContinent).setId(Integer.parseInt(ligne));;
				newContinent=false;
			}
			else if(!newCellule){
				Cellule cellule = new Cellule();
				cellule.setId(Integer.parseInt(ligne));
				newCellule=true;
				comptCellule++;
				graphe.getContinent(comptContinent).addCellule(cellule);
			}
			else{
				newCellule=false;
				Cellule cellule = graphe.getContinent(comptContinent).getCellule(comptCellule);
				String[] voisins = ligne.split(" ");
				for(String voisin : voisins){
					cellule.addVoisin(Integer.parseInt(voisin));
				}
			}
		}
		buff.close(); 
		}
		 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return graphe;
	}
}

