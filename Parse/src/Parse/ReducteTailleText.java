package Parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ReducteTailleText {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String f = "C:/DATA/ISIC/PDR/PDR/listeDistance.txt";

		try {

			PrintWriter printwriter = new PrintWriter(new FileOutputStream(f));

			write("C:/DATA/ISIC/PDR/PDR/listeDistanceFinalSave.txt", printwriter);
			

			printwriter.close();
		}

		catch (Exception ex) {

			System.out.println("Error clear file" + f);

		}

	}

	public static void write(String f, PrintWriter printwriter) throws IOException {
		InputStream flux;
		try {
			flux = new FileInputStream(f);

			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader buff = new BufferedReader(lecture);
			String ligne;
			while ((ligne = buff.readLine()) != null) {
				Boolean change = true;
				while (change) {
					int i = ligne.indexOf("BlocDistance");
					if (i == -1) {
						change = false;
					}
					else{
						ligne = ligne.substring(0, i)+ "BD" + ligne.substring(i+12);
					}
				}
				change = true;
				while (change) {
					int i = ligne.indexOf("blocDistance");
					if (i == -1) {
						change = false;
					}
					else{
						ligne = ligne.substring(0, i)+ "bD" + ligne.substring(i+12);
					}
				}
				change = true;
				while (change) {
					int i = ligne.indexOf("distance");
					if (i == -1) {
						change = false;
					}
					else{
						ligne = ligne.substring(0, i)+ "d" + ligne.substring(i+8);
					}
				}
				change = true;
				while (change) {
					int i = ligne.indexOf("Distance");
					if (i == -1) {
						change = false;
					}
					else{
						ligne = ligne.substring(0, i)+ "D" + ligne.substring(i+8);
					}
				}
				change = true;
				System.out.println(ligne);
				printwriter.println(ligne);

			}
			buff.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
