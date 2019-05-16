package Parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import Model.Graphe;
import Model.ListDistance;

public class ParseAllTextListDistanceToOne {

	public static void main(String[] args) {

		String f = "C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceFinal.txt";
		String s1 = "ArrayList<ListDistance> list = new ArrayList<ListDistance>();";
		String s2 = "ListDistance listDistance = new ListDistance();";
		String s3 = "list = list.add(listDistance) ;";

		try {

			PrintWriter printwriter = new PrintWriter(new FileOutputStream(f));
			

			printwriter.println("class CreerDistance {");
			printwriter.println("");
			printwriter.println("public static ArrayList<ListDistance> creer() {");
			
			

			printwriter.println(s1);

			printwriter.println(s2);
			write("C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceAmeriqueNord.txt", printwriter,s1 , s2, s3);
			printwriter.println(s3);
			
			printwriter.println(s2);
			write("C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceAmeriqueSud.txt", printwriter,s1 , s2, s3);
			printwriter.println(s3);
			
			printwriter.println(s2);
			write("C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceAntartique.txt", printwriter,s1 , s2, s3);
			printwriter.println(s3);
			
			printwriter.println(s2);
			write("C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceEurasie.txt", printwriter,s1 , s2, s3);
			printwriter.println(s3);
			
			printwriter.println(s2);
			write("C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceJapon.txt", printwriter, s1, s2, s3);
			printwriter.println(s3);
			
			
			
			
			printwriter.println("return list;");
			printwriter.println("}");
			printwriter.println("}");

			printwriter.close();

		}

		catch (Exception ex) {

			System.out.println("Error clear file" + f);

		}

	}

	public static void write(String f, PrintWriter printwriter,String s1, String s2, String s3) throws IOException {
		InputStream flux;
		try {
			flux = new FileInputStream(f);


			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader buff = new BufferedReader(lecture);
			String ligne;
			while ((ligne = buff.readLine()) != null) {
				if((!ligne.equals(s1))&&(!ligne.equals(s2))&&(!ligne.equals(s3))){
					printwriter.println(ligne);
				}
			}
			buff.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
