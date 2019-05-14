package Parse;

import java.io.IOException;
import java.util.*;
import Model.*;

public class ParseDataToListDistance {
	static int[] japon = new int[] { 143, 149, 150 };
	static int[] ameriqueNord = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21,
			22, 23, 27, 28, 29, 37, 38, 43, 46, 47, 48, 49 };
	static int[] ameriqueSud = new int[] { 18, 24, 25, 26, 30, 31, 32, 33, 34, 35, 36, 39, 40, 41, 42, 44, 45, 50, 51,
			54, 55, 56, 60, 61, 62, 63, 64, 65, 66, 71, 72, 73, 74, 75, 76, 77, 83, 84, 85, 86, 87, 88, 95, 96 };
	static int[] antartic = new int[] { 57, 67, 78, 89, 97, 104, 113 };
	static int[] eurasie = new int[] { 52, 53, 58, 59, 68, 69, 70, 79, 80, 81, 82, 90, 91, 92, 93, 94, 98, 99, 100, 101,
			102, 103, 105, 106, 107, 108, 109, 110, 111, 112, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
			125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 144, 145, 146,
			147, 148, 151, 152, 153 };

	public static ArrayList<ListDistance> parse(Graphe graphe) throws IOException {
		ArrayList<ListDistance> list = new ArrayList<ListDistance>();

		for (Continent continent : graphe.getContinents()) {
			ListDistance listDistance = new ListDistance();
			if (continent.size() == 3) {
				listDistance = createListUnContinent(japon, continent);
			} else if (continent.size() == 33) {
				listDistance = createListUnContinent(ameriqueNord, continent);
			} else if (continent.size() == 44) {
				listDistance = createListUnContinent(ameriqueSud, continent);
			} else if (continent.size() == 7) {
				listDistance = createListUnContinent(antartic, continent);
			} else if (continent.size() == 67) {
				listDistance = createListUnContinent(eurasie, continent);
			} else {
				System.out.println("pb : size = " + continent.size());
			}

			list.add(listDistance);
		}

		return list;
	}

	public static ListDistance createListUnContinent(int[] cont, Continent continent) {
		ListDistance listDistance = new ListDistance();

		for (int i = 0; i < cont.length; i++) {
			Distance distance = new Distance(cont[i]);
			for (int j = 0; j < cont.length; j++) {
				if (i != j) {
					int dist = distance(cont[i], cont[j], continent);
					distance.getDistByDistance(dist).add(cont[j]);
				}
			}
			listDistance.add(distance);
		}

		return listDistance;
	}

	public static int distance(int id1, int id2, Continent continent) {
		ArrayList<Integer> parcouru = new ArrayList<Integer>();
		return parcour(id2, id1, continent, parcouru, 0);
	}

	public static int parcour(int idCherche, int idPos, Continent continent, ArrayList<Integer> parcouru, int compt) {
		if (compt > 21) {
			return -1;
		}
		if (continent.getCelluleById(idPos).getVoisins().contains(idCherche)) {
			return compt + 1;
		}
		int save = 21;
		for (Cellule cellule : continent.getCellules()) {
			if (!parcouru.contains(cellule.getId())) {
				parcouru.add(cellule.getId());
				int res = parcour(idCherche, cellule.getId(), continent, parcouru, compt + 1);
				if (save > res) {
					save = res;
				}
			}
		}
		if (save != 21) {
			return save;
		}

		return -1;
	}

}
