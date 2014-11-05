package cser.utils;
/** Simple Program to write a text file
*/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;





public class WriteText{
	public static void write(String text){
		try {
			FileWriter outFile = new FileWriter(GlobalConfig.db_folder+ GlobalConfig.seperator +"log.txt",true);
			PrintWriter out = new PrintWriter(outFile);
			
			// Also could be written as follows on one line
			// Printwriter out = new PrintWriter(new FileWriter(args[0]));
		
			// Write text to file
			out.println(":"+text);
			System.out.println(text);

			out.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public static void write(int i){
		write(""+i);
	}

	public static void print(Set<String> list) {
	    for(String var: list){
	    	System.out.println(var);
	    }
		
	}
	}