package Parse;

import java.io.*;


public class ParseCode {

	public static void main(String[] args) {

		String f="C:/DATA/ISIC/PDR/PDR/platinumRift.java";

		try { 

			
			PrintWriter printwriter = new PrintWriter(new FileOutputStream(f)); 

			printwriter.println("import java.util.*;"); 

			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/Player.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src//Model/Tools.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src//Model/Graphe.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src//Model/Continent.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/Cellule.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/ListDistance.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/Distance.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/BlocDistance.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/listeDistanceFinal.txt", printwriter);

			printwriter.close(); 

			} 

			catch (Exception ex) { 

				System.out.println("Error clear file"+f); 

			} 


	}
	
	public static void write(String f, PrintWriter printwriter) throws IOException{
		InputStream flux;
		try {
			flux = new FileInputStream(f);
		
		InputStreamReader lecture=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(lecture);
		String ligne;
		while ((ligne=buff.readLine())!=null){
			if((!ligne.equals("package Model;"))&&(!ligne.equals("import java.util.*;"))){
				if(ligne.equals("public class Player {")){
					printwriter.println("class Player {"); 
				}
				else if(ligne.equals("public class Graphe {")){
					printwriter.println("class Graphe {"); 
				}
				else if(ligne.equals("public class Tools{")){
					printwriter.println("class Tools {"); 
				}
				else if(ligne.equals("public class Continent {")){
					printwriter.println("class Continent {"); 
				}
				else if(ligne.equals("public class Cellule{")){
					printwriter.println("class Cellule {"); ; 
				}
				else if(ligne.equals("public class Distance {")){
					printwriter.println("class Distance {"); ; 
				}
				else if(ligne.equals("public class ListDistance {")){
					printwriter.println("class ListDistance {"); 
				}
				else{
					printwriter.println(ligne); 
				}
				
			}
			
		}
		buff.close(); 
		}
		 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
