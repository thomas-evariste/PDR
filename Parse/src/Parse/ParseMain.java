package Parse;
import java.io.IOException;
import java.util.ArrayList;

import Model.*;

public class ParseMain {

	public static void main(String[] args) throws IOException {
		Graphe graphe = ParseTexteToGraphe.parse();
		System.out.println("/////////////////////////////////////////////////////////////////////////////");
		ArrayList<ListDistance> list = CreateListDistance.create(graphe);
	}

}
