package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

public class CSE535Assignment {

	public static void main(String[] args) 
	{
		long startTime = System.nanoTime();
		
		String input_file_name =  args[0];
		String log_file_name = args[1];
		int top_k = Integer.parseInt(args[2]);
		String query_file_name = args[3];
		
		Indexer i = new Indexer(input_file_name,top_k);
		i.getTerm("-day");
		
		//i.printTopK();
		
	
		long endTime = System.nanoTime();
		System.out.println();
		System.out.println("Took "+(endTime - startTime) + " ns"); 

	}
	
	
	public static ArrayList<ArrayList<String>> parseQueryFile(String file_name)
	{
		ArrayList< ArrayList<String> > result = new ArrayList<ArrayList<String>>();
		File f = new File(file_name);
		
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    BufferedReader br = new BufferedReader(fr);
		String line;
	    try {
			while((line = br.readLine()) != null)
			{
				ArrayList<String> termList = new ArrayList<String>(Arrays.asList(line.split(" "))); 
				result.add(termList);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
	    try 
	    {
			br.close();
		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try 
	    {
			fr.close();
		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	

}

