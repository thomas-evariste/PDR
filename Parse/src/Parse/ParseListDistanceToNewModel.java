package Parse;

import java.io.*;

public class ParseListDistanceToNewModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String f = "C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistanceNewModel.txt";

		try {

			PrintWriter printwriter = new PrintWriter(new FileOutputStream(f));

			write("C:/Users/simon/OneDrive/Bureau/ISIC/PDR/listeDistance.txt", printwriter);

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
			String ligneAddListD = "";
			String ligneAddListD_D = "";
			String ligneAddD_BlocDistance = "";
			String ligneAddD = "";
			String ligneAddBD = "";
			String ligneAddBD_int = "";
			int comptListD = 0;
			int comptD = 0;
			int comptBD = 0;
			int i;
			int j;
			int compt = 0;
			int comptttt=0;
			int compttttttttttt=0;
			int compttttttttttttttttt=0;
			while ((ligne = buff.readLine()) != null) {
				i = ligne.indexOf("(") + 1;
				j = ligne.indexOf(")");

				if (ligne.equals("class CreerD {") || ligne.equals("ArrayList<ListD> list = new ArrayList<ListD>();")
						|| ligne.equals("public static ArrayList<ListD> creer() {")
						|| ligne.equals("}")|| ligne.equals("}")|| ligne.equals("return list;")) {
					printwriter.println(ligne);
					compt++;
				} else if (ligne.equals("ListD listD = new ListD();")) {
					compttttttttttttttttt++;
					ligneAddListD = ligneAddListD + "ListD listD"+compttttttttttttttttt+"=new ListD(di"+compttttttttttt+"); ";
					ligneAddListD_D = ligneAddListD_D + "D[] di"+compttttttttttt+"={";
					compttttttttttt++;
				} else if (ligne.indexOf("D d = new D") != -1) {
					ligneAddD = ligneAddD + "D d" + comptListD + "=new D(" + ligne.substring(i, j) + ",b"+comptttt+");";
					ligneAddD_BlocDistance = ligneAddD_BlocDistance + "int[][] b"+comptttt+"={";
					comptttt++;
				} else if (ligne.indexOf("BD bD = new BD") != -1) {
					ligneAddD_BlocDistance = ligneAddD_BlocDistance + "{";
				} else if (ligne.indexOf("bD = bD.add") != -1) {
					comptBD++;
					if (comptBD != 1) {
						ligneAddD_BlocDistance = ligneAddD_BlocDistance + ",";
					}
					ligneAddD_BlocDistance = ligneAddD_BlocDistance + "" + ligne.substring(i, j);
				} else if (ligne.indexOf("d = d.add") != -1) {
					ligneAddD_BlocDistance = ligneAddD_BlocDistance + "}";
					comptBD = 0;

					ligneAddD_BlocDistance = ligneAddD_BlocDistance + ",";
					// ligneAddD_BlocDistance = ligneAddD_BlocDistance + "b" +
					// comptD;
					// comptD++;
				} else if (ligne.indexOf("listD = listD.add") != -1) {

					ligneAddD_BlocDistance = ligneAddD_BlocDistance.substring(0, ligneAddD_BlocDistance.length() - 1)
							+ "};";
					printwriter.println(ligneAddD_BlocDistance);
					printwriter.println(ligneAddD);
					ligneAddD_BlocDistance = "";
					ligneAddD = "";
					comptD = 0;

					if ((comptListD != 0)&&(comptListD != 3)&&(comptListD != 10)&&(comptListD != 43)&&(comptListD != 87)) {
						ligneAddListD_D = ligneAddListD_D + ",";
					}
					ligneAddListD_D = ligneAddListD_D + "d" + comptListD;
					comptListD++;
				} else if (ligne.indexOf("list = list.add(listD)") != -1) {

					ligneAddListD_D = ligneAddListD_D + "};";
					printwriter.println(ligneAddListD_D);
					printwriter.println(ligneAddListD);
					ligneAddListD_D = "";
					ligneAddListD = "";

					printwriter.println(ligne.substring(7,21)+compttttttttttttttttt+ligne.substring(21));
				}

				// printwriter.println(ligne);

			}
			buff.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
