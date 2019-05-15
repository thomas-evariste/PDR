package Parse;
import java.io.IOException;
import java.util.ArrayList;

import Model.*;

public class ParseMain {

	public static void main(String[] args) throws IOException {
		Graphe graphe = ParseTexteToGraphe.parse();
		System.err.println(graphe.getCelluleById(20).getVoisins().size());
		ArrayList<ListDistance> list = ParseDataToListDistance.parse(graphe);
		ParseListDistanceToTexte.parse(list); 
		
		System.err.println("c'est fini");
	}

}
