package Parse;
import java.io.IOException;
import java.util.ArrayList;

import Model.*;

public class ParseMain {

	public static void main(String[] args) throws IOException {
		Graphe graphe = ParseTexteToGraphe.parse();
		ArrayList<ListDistance> list = ParseDataToListDistance.parse(graphe);
	}

}
