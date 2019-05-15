package Parse;

import java.io.*;

public class ParseCode {

	public static void main(String[] args) {

		String f = "C:/DATA/ISIC/PDR/PDR/platinumRift.java";

		try {

			PrintWriter printwriter = new PrintWriter(new FileOutputStream(f));

			printwriter.println("import java.util.*;");

			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/Player.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src//Model/Tools.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src//Model/Graphe.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src//Model/Continent.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/Cellule.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/ListD.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/Parse/src/Model/D.java", printwriter);
			write("C:/DATA/ISIC/PDR/PDR/listeDistanceNewModel.txt", printwriter);

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
				if ((!ligne.equals("package Model;")) && (!ligne.equals("import java.util.*;"))
						&& (ligne.indexOf("//") == -1)) {

					Boolean change = true;
					while (change) {
						int i = ligne.indexOf("	");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(" =");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(" <");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(" >");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(" +");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(" :");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(" {");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i) + "" + ligne.substring(i + 1);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("= ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 2);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("; ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 2);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("< ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 2);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("> ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 2);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("+ ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 2);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf(": ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 2);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("for ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+3) + "" + ligne.substring(i + 4);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("if ");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+2) + "" + ligne.substring(i + 3);
						}
					}
					


					change = true;
					while (change) {
						int i = ligne.indexOf("continent");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 9);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("Continent");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "" + ligne.substring(i + 9);
						}
					}

					change = true;
					while (change) {
						int i = ligne.indexOf("cellule");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "e" + ligne.substring(i + 7);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("Cellule");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "E" + ligne.substring(i + 7);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("Graphe");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "G" + ligne.substring(i + 6);
						}
					}
					change = true;
					while (change) {
						int i = ligne.indexOf("graphe");
						if (i == -1) {
							change = false;
						} else {
							ligne = ligne.substring(0, i+1) + "g" + ligne.substring(i + 6);
						}
					}
					
					
					
					
					
					if(ligne.indexOf("public static void main")==-1){
						change = true;
						while (change) {
							int i = ligne.indexOf("public");
							if (i == -1) {
								change = false;
							} else {
								ligne = ligne.substring(0, i) + "" + ligne.substring(i + 7);
							}
						}
						
					}
						
					if(ligne.length()!=0)
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
