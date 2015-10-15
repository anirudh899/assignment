package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CSE535Assignment {

	public static void main(String[] args) 
	{
		long startTime = System.nanoTime();
		
		String input_file_name =  args[0];
		String log_file_name = args[1];
		int top_k = Integer.parseInt(args[2]);
		String query_file_name = args[3];
		
		Indexer index = new Indexer(input_file_name,top_k);
	//	ArrayList<ArrayList<String>> queryList = parseQueryFile(query_file_name);
		ArrayList<ArrayList<String>> queryList = new ArrayList<ArrayList<String>>();
		ArrayList<String> a = new ArrayList<String>();
		a.add("Africa");
		a.add("Agenci");
		queryList.add(a);
	
		
		for(int i = 0 ; i < queryList.size(); i++)
		{
			ArrayList<String> termList = queryList.get(i);
			Operate(termList,log_file_name,index);
		}
		
		
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
	
	public static void Operate(ArrayList<String> termList, String outputFile,Indexer index)
	{
		ArrayList<TermData> list = new ArrayList<TermData> ();
		for(int j = 0 ; j < termList.size(); j++)
		{
			
			String term = termList.get(j);
			TermData td = index.getTermData(term);
			
			
			
			if(td == null)
			{
				// Add logic to print NULL;
				
			}
			else
			{
				//print to log both the lists with get posting
			/*	System.out.println(term);
				for(int i = 0 ; i < td.postingsListSortedByDoc.size(); i++)
				{
					System.out.println(td.postingsListSortedByDoc.get(i));
					
				}
				System.out.println("########");
				{
					for(int i = 0 ; i < td.postingsListSortedByFrequency.size();i++)
					{
						System.out.println(td.postingsListSortedByFrequency.get(i).docId + " " + td.postingsListSortedByFrequency.get(i).term_frequency);
						
					}
				}
				*/
				
			
				list.add(td);
			}
			
		}
		
		TaatOr(list,outputFile);
		
		
		
		
		
	}
	
	public static void TaatOr(ArrayList<TermData> list, String outputFile)
	{
		int comparisions = 0;
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
			
			LinkedList<Posting> tempList = list.get(0).postingsListSortedByFrequency;
			
			for(int i = 1; i < list.size(); i++)
			{
				LinkedList<Posting> curList = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> toAddList = new LinkedList<Posting>();
				for(int cur = 0; cur < curList.size(); cur++)
				{
					String curDoc = curList.get(cur).docId;
					int p = 0;
					for(p = 0 ; p < tempList.size(); p++)
					{
						comparisions ++;
						if(curDoc.equals(tempList.get(p).docId))
							break;
					}
					
					if(p == tempList.size())
						toAddList.add(curList.get(cur));
					
				}
				
				tempList.addAll(toAddList);
				
			}
			
			
			{
				Collections.sort(tempList, new docIdComparator());
				for(int i = 0 ; i < tempList.size(); i++)
						System.out.println(tempList.get(i).docId);
				System.out.println("comparisions = " + comparisions);
					
				//print the comparisions used, documents found (tempList.size()) and seconds used
				// to the output file.
				
				
			}
			
			// now optimized
			Collections.sort(list, new CountComparator());
			for(int i = 0 ; i < list.size(); i++)
			{
				System.out.println("count : " + list.get(i).count);
				
				
			}
			comparisions = 0 ;
			
			//repeat.
			LinkedList<Posting> tempList1 = list.get(0).postingsListSortedByFrequency;
			
			for(int i = 1; i < list.size(); i++)
			{
				LinkedList<Posting> curList = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> toAddList = new LinkedList<Posting>();
				for(int cur = 0; cur< curList.size(); cur++)
				{
					String curDoc = curList.get(cur).docId;
					int p = 0;
					for(p = 0 ; p < tempList1.size(); p++)
					{
						comparisions ++;
						if(curDoc.equals(tempList1.get(p).docId))
							break;
					}
					
					if(p == tempList1.size())
						toAddList.add(curList.get(cur));
					
				}
				
				tempList1.addAll(toAddList);
			}	
			
			{
				Collections.sort(tempList1, new docIdComparator());
				for(int i = 0 ; i < tempList1.size(); i++)
						System.out.println(tempList1.get(i).docId);
				System.out.println("comparisions new = " + comparisions);

				// print optimal comparisions and final result here.
			}
		
		}
		
	}
	
	public static void TaatAnd(ArrayList<TermData> list, String outputFile)
	{
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
			
			
			
			
			
			
			
			
		}
		
	}
	public static void DaatOr(ArrayList<TermData> list, String outputFile)
	{
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
			
			
			
			
		}
		
	}
	public static void DaatAnd(ArrayList<TermData> list, String outputFile)
	{
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
			
			
			
			
		}
		
	}

	
	

}

class CountComparator implements Comparator<TermData>
{

	public int compare(TermData t1, TermData t2) 
	{
		return t1.count - t2.count;

	}
}

