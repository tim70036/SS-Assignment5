package assignment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Reassembler {

	public static void main(String[] args) {
		String[][] word = new String[20][20];
		try {
			// Read File of output.txt
			Scanner scanner = new Scanner(new File(args[0]));

			// Read output.txt, and record the ,row ,col, data in word
			while(scanner.hasNext())
			{
				String file = scanner.next();
				String data = scanner.next();

				// Place the word
				int row = Integer.parseInt(file.substring(0, 2));
				int col = Integer.parseInt(file.substring(3,5));
				//System.out.println(data + " : " + row + " " +  col);
				word[row][col] = data;
			}

			// Output
			for(int i=1 ; i<=15 ; i++)
			{
				for(int j=1 ; j<=15 ; j++)
				{
					if(word[i][j] != null)
						System.out.print(word[i][j] + " ");
					else
						System.out.print("____ ");
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
