package Parse;
import java.io.*;

import Model.*;

public class ParseTexteToGraphe {

	public static void main(String[] args) throws IOException {
		InputStream flux;
		try {
			flux = new FileInputStream("C:/DATA/ISIC/PDR/PDR/donneesGraphe.txt");
		
		InputStreamReader lecture=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(lecture);
		String ligne;
		while ((ligne=buff.readLine())!=null){
			System.out.println(ligne);
			Graphe graphe =new Graphe();
		}
		buff.close(); 
		}
		 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

