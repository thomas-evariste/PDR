package Parse;

import java.io.*;
import java.util.*;

import Model.*;

public class ParseListDistanceToTexte {

	public static void parse(ArrayList<ListDistance> list) {

		String f = "C:/DATA/ISIC/PDR/PDR/listeDistanceAmeriqueSud.txt";

		String ligne = "";

		try {

			PrintWriter printwriter = new PrintWriter(new FileOutputStream(f));

			ligne = "ArrayList<ListDistance> list = new ArrayList<ListDistance>();";
			printwriter.println(ligne);
			for (ListDistance listDistance : list) {
				ligne = "ListDistance listDistance = new ListDistance();";
				printwriter.println(ligne);
				for (Distance distance : listDistance.getDistances()) {
					ligne = "	Distance distance = new Distance("+distance.getId()+");";
					printwriter.println(ligne);
					for (BlocDistance blocDistance : distance.getDists()) {
						ligne = "		BlocDistance blocDistance = new BlocDistance("+blocDistance.getDistance()+");";
						printwriter.println(ligne);
						for (int id : blocDistance.getIds()) {
							ligne = "			blocDistance = blocDistance.add("+id+") ;";
							printwriter.println(ligne);
						}
						ligne = "		distance = distance.add(blocDistance) ;";
						printwriter.println(ligne);
					}
					ligne = "	listDistance = listDistance.add(distance) ;";
					printwriter.println(ligne);
				}
				ligne = "list = list.add(listDistance) ;";
				printwriter.println(ligne);
			}

			printwriter.close(); 

		}

		catch (Exception ex) {

			System.out.println("Error clear file" + f);

		}

	}
}
