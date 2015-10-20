package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CSE535Assignment {

	
	public static void main(String[] args) 
	{
		
		
		String input_file_name =  args[0];
		String log_file_name = args[1];
		int top_k = Integer.parseInt(args[2]);
		String query_file_name = args[3];
		
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(log_file_name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.setOut(out);
		Indexer index = new Indexer(input_file_name,top_k);
		ArrayList<ArrayList<String>> queryList = parseQueryFile(query_file_name);
		
	
		for(int i = 0 ; i < queryList.size(); i++)
		{
			ArrayList<String> termList = queryList.get(i);
			Operate(termList,log_file_name,index);
		}
		
		
	
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
		boolean ignoreAnd = false;
		for(int j = 0 ; j < termList.size(); j++)
		{
			
			String term = termList.get(j);
			TermData td = index.getTermData(term);
			System.out.println("\nFUNCTION: getPostings " + term);
			
			
			
			
			if(td == null)
			{
				// Add logic to print NULL;
				System.out.print("term not found");
				ignoreAnd = true;
			}
			else
			{
				System.out.print("Ordered by doc IDs: ");
				for(int i=0;i<td.postingsListSortedByDoc.size(); i++)
				{
					System.out.print(td.postingsListSortedByDoc.get(i));
					if(i != td.postingsListSortedByDoc.size() - 1)
						System.out.print(", ");
				}
				System.out.println("");
				
				System.out.print("Ordered by TF: ");
				for(int i = 0; i < td.postingsListSortedByFrequency.size();i++)
				{
					
					System.out.print(td.postingsListSortedByFrequency.get(i).docId );
					if(i != td.postingsListSortedByFrequency.size() - 1)
						System.out.print(", ");
				
				}
				
				list.add(td);
			}
			
		}
		System.out.print("\nFUNCTION: termAtATimeQueryAnd: ");
		for(int i =0; i < termList.size(); i++)
		{
			System.out.print(termList.get(i));
			if(i != termList.size() - 1)
				System.out.print(", ");
		}
		TaatAnd(list,outputFile,ignoreAnd);
		System.out.print("\nFUNCTION: termAtATimeQueryOr: ");
		for(int i =0; i < termList.size(); i++)
		{
			System.out.print(termList.get(i));
			if(i != termList.size() - 1)
				System.out.print(", ");
		}
		TaatOr(list,outputFile);
		System.out.print("\nFUNCTION: docAtATimeQueryAnd: ");
		for(int i =0; i < termList.size(); i++)
		{
			System.out.print(termList.get(i));
			if(i != termList.size() - 1)
				System.out.print(", ");
		}
		DaatAnd(list,outputFile,ignoreAnd);
		System.out.print("\nFUNCTION: docAtATimeQueryOr: ");
		for(int i =0; i < termList.size(); i++)
		{
			System.out.print(termList.get(i));
			if(i != termList.size() - 1)
				System.out.print(", ");
		}
		DaatOr(list,outputFile);
	
		
	}
	
	public static void TaatOr(ArrayList<TermData> termDatalist, String outputFile)
	{
		ArrayList<TermData> list = new ArrayList<TermData>(termDatalist);
		int comparisions = 0;
		if(list.size() == 0)
		{
			
			System.out.println("\n" + "0 documents were found");
			System.out.println("0 comparisions were made");
			System.out.println("0 seconds were used");
			System.out.print("Result: ");
			
		}
		else
		{
			
			long startTime1 = System.currentTimeMillis();
			LinkedList<Posting> tempList = new LinkedList<Posting>(list.get(0).postingsListSortedByFrequency);
			
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
				long endTime1= System.currentTimeMillis();
				
				System.out.println("\n" + tempList.size() + " documents were found");
				System.out.println( comparisions + " comparisions were made");
				System.out.println(((endTime1 - startTime1)/1000) + " seconds were used");
			
			}
			
			// now optimized
			Collections.sort(list, new CountComparator());
	
			comparisions = 0 ;
			
			//repeat.
			LinkedList<Posting> tempList1 = new LinkedList<Posting>(list.get(0).postingsListSortedByFrequency);
			
			for(int i = 1; i < list.size(); i++)
			{
				LinkedList<Posting> curList1 = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> toAddList1 = new LinkedList<Posting>();
				for(int cur = 0; cur< curList1.size(); cur++)
				{
					String curDoc = curList1.get(cur).docId;
					int p = 0;
					for(p = 0 ; p < tempList1.size(); p++)
					{
						comparisions ++;
						if(curDoc.equals(tempList1.get(p).docId))
							break;
					}
					
					if(p == tempList1.size())
						toAddList1.add(curList1.get(cur));
					
				}
				
				tempList1.addAll(toAddList1);
			}	
			
			{
				Collections.sort(tempList1, new docIdComparator());
				System.out.println(comparisions + " comparisons are made with optimization");
				System.out.print("Result: ");
				for(int i = 0 ; i < tempList1.size(); i++)
				{
					System.out.print(tempList1.get(i).docId);
					if(i != tempList1.size() - 1)
						System.out.print(", ");
				}
			}
		
		}
		
	}
	
	public static void TaatAnd(ArrayList<TermData> termDatalist, String outputFile, boolean ignoreAnd)
	{
		ArrayList<TermData> list = new ArrayList<TermData>(termDatalist);
		
		if(ignoreAnd == true)
		{
			
			System.out.println("\n"  + "0 documents were found");
			System.out.println("0 comparisions were made");
			System.out.println("0 seconds were used");
			System.out.print("Result: ");
		}
		
		else
		{
			long startTime1 = System.currentTimeMillis();
			LinkedList<Posting> tempList = new LinkedList<Posting>(list.get(0).postingsListSortedByFrequency);
			int comparisions = 0;
			for(int i = 1 ; i < list.size(); i++)
			{
				LinkedList<Posting> curList = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> newTemp = new LinkedList<Posting>();
				for(int p = 0 ; p < tempList.size(); p++)
				{
					String tempDoc = tempList.get(p).docId;
					
					for(int cur = 0; cur < curList.size(); cur++)
					{
						
						comparisions ++;	
						
						String curDoc = curList.get(cur).docId;
						if(curDoc.equals(tempDoc))
						{
							newTemp.add(curList.get(cur));
							break;
						}
						
					}
			
				}
				
				tempList.clear();
				tempList.addAll(newTemp);
				
			}
			
			{
				// print to output file here.
				Collections.sort(tempList, new docIdComparator());
				long endTime1 = System.currentTimeMillis();
				
				System.out.println("\n" + tempList.size() + " documents were found");
				System.out.println( comparisions + " comparisions were made");
				System.out.println(((endTime1 - startTime1)/1000) + " seconds were used");
				
				
			}
			
			// now optimized
			Collections.sort(list, new CountComparator());
			comparisions = 0;
			
			LinkedList<Posting> tempList1 = new LinkedList<Posting>(list.get(0).postingsListSortedByFrequency);
			
			for(int i = 1 ; i < list.size(); i++)
			{
				LinkedList<Posting> curList1 = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> newTemp1 = new LinkedList<Posting>();
				
				
				for(int p = 0 ; p < tempList1.size(); p++)
				{
					String tempDoc = tempList1.get(p).docId;
					for(int cur = 0; cur < curList1.size(); cur++)
					{
						String curDoc = curList1.get(cur).docId;
						comparisions ++;	
					
						if(curDoc.equals(tempDoc))
						{
							newTemp1.add(curList1.get(cur));
							break;
						}
						
					}
			
				}
				
				tempList1.clear();
				tempList1.addAll(newTemp1);
				
			}
			
			{
				// print to output file here.
				Collections.sort(tempList1, new docIdComparator());
				System.out.println(comparisions + " comparisons are made with optimization");
				System.out.print("Result: ");
				for(int i = 0 ; i < tempList1.size(); i++)
				{
					System.out.print(tempList1.get(i).docId);
					if(i != tempList1.size() - 1)
						System.out.print(", ");
				}
				
			}
			
			
		}
		
	}
	public static void DaatAnd(ArrayList<TermData> list, String outputFile,boolean ignoreAnd)
	{
		// UPDATE COMPARISIONS PROPERLY, should consider isValid function also 
		int comparisions = 0;
		if(ignoreAnd == true)
		{
			// print appropriate message in log file
			System.out.println("\n"  + "0 documents were found");
			System.out.println("0 comparisions were made");
			System.out.println("0 seconds were used");
			System.out.print("Result: ");
		}
		
		else
		{
			long startTime = System.currentTimeMillis();
			//get max of first indices of all docs;
			// then move the first indices of the remaining docs till they equal or exceed the max.
			//if all of them point to max, then add the max to the result and move that mx forward.
			
			
			ArrayList<String> result = new ArrayList<String>();
			ArrayList<LinkedList<String>> listOfLists = new ArrayList<LinkedList<String>>();			
			for(int i = 0 ; i < list.size(); i++)
				listOfLists.add(list.get(i).postingsListSortedByDoc);
		
	
			ArrayList<Integer> listOfIndices = new ArrayList<Integer>();
			for(int i = 0 ; i < listOfLists.size(); i++)
				listOfIndices.add(0);
				
			while(isValid2(listOfLists,listOfIndices))
			{
				String maxString = "";
				int max_index = -1;
				
				for(int i = 0 ; i < listOfLists.size();i++)
				{
					int index = listOfIndices.get(i);
					LinkedList<String> docList = listOfLists.get(i);
					
					if(index < docList.size())
					{
						String s = docList.get(index);
						comparisions++;
						if(s.compareTo(maxString) > 0)
						{
							maxString = new String(s);
							max_index = i;
						}
					
					}
				
				}
				
				// move forward.
				for(int i = 0; i < listOfLists.size(); i++)
				{
					if(i != max_index)
					{
						LinkedList<String> docList = listOfLists.get(i);
						int j = listOfIndices.get(i);
						while(j<docList.size() && docList.get(j).compareTo(maxString) < 0)
						{
							comparisions++;
							j++;
						}
						
						listOfIndices.set(i, j);
					}
					
				}
				
				// now check.
				int j = 0;
				for(j = 0; j < listOfLists.size(); j++)
				{
					int index = listOfIndices.get(j);
					LinkedList<String> docList = listOfLists.get(j);
					
					if(index >= docList.size())
					{
						// finished !!.. print and return.
						
						long endTime = System.currentTimeMillis();
						System.out.println("\n" + result.size() + " documents are found");
						System.out.println(comparisions + " comparisions are made");
						System.out.println((endTime-startTime)/1000 + " seconds are used");
						System.out.print("Result: ");
						for(int i = 0 ; i < result.size(); i++)
						{
							System.out.print(result.get(i));
							if(i != result.size() - 1)
								System.out.print(", ");
						}
						return;
					}
					
					else
					{
						
						String s = docList.get(index);
						comparisions++;
						if(s.equals(maxString) == false)
							break;
						
					}
					
				}
				
				if(j == listOfLists.size())
					result.add(maxString);
			
				// setmaxIndex
				listOfIndices.set(max_index, listOfIndices.get(max_index) + 1);
			
			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("\n" + result.size() + " documents are found");
			System.out.println(comparisions + " comparisions are made");
			System.out.println((endTime-startTime)/1000 + " seconds are used");
			System.out.print("Result: ");
			for(int i = 0 ; i < result.size(); i++)
			{
				System.out.print(result.get(i));
				if(i != result.size() - 1)
					System.out.print(", ");
			}
					
		}
		
	}
	public static void DaatOr(ArrayList<TermData> list, String outputFile)
	{
		// UPDATE COMPARISIONS PROPERLY, should consider isValid function also.
		
		ArrayList<String> result = new ArrayList<String>();
		int comparisions = 0;
		if(list.size() == 0)
		{
			System.out.println("\n" + "0 documents are found");
			System.out.println("0 comparisions are made");
			System.out.println("0 seconds are used");
			System.out.print("Result: ");
		}
		else
		{
			long startTime = System.currentTimeMillis();
			ArrayList<LinkedList<String>> listOfLists = new ArrayList<LinkedList<String>>();			
				for(int i = 0 ; i < list.size(); i++)
					listOfLists.add(list.get(i).postingsListSortedByDoc);
			
		
			ArrayList<Integer> listOfIndices = new ArrayList<Integer>();
				for(int i = 0 ; i < listOfLists.size(); i++)
					listOfIndices.add(0);				
			
			
			while(isValid(listOfLists,listOfIndices) == true)
			{
				String minString = "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
				int minIndex = -1;
				
				for(int i = 0 ; i < listOfLists.size();i++)
				{
					int index = listOfIndices.get(i);
					LinkedList<String> docList = listOfLists.get(i);
										
					if(index < docList.size())
					{
						String s = docList.get(index);
						if(s != null)
						{
							comparisions++;
							if(s.compareTo(minString) < 0)
							{
								minString = new String(s);
								minIndex = i;
							}													
						}
					}
				}
				
				if(minIndex == -1)
					break;
				else
				{			
					result.add(minString);
					for(int i = 0 ; i < listOfLists.size(); i++)
					{
						int index = listOfIndices.get(i);
						LinkedList<String> docList = listOfLists.get(i);
						if(index < docList.size())
						{
							comparisions++;
							String s = docList.get(index);
							if(s.equals(minString))
								listOfIndices.set(i,index+1);
						}
						
					}					
						
				}			
				
			}
			long endTime = System.currentTimeMillis();
			
			
			System.out.println("\n" + result.size() + " documents are found");
			System.out.println(comparisions + " comparisions are made");
			System.out.println((endTime-startTime)/1000 + " seconds are used");
			System.out.print("Result: ");
			for(int i = 0 ; i < result.size(); i++)
			{
				System.out.print(result.get(i));
				if(i != result.size() - 1)
					System.out.print(", ");
			}
		
		}
		
		
	}

	public static boolean isValid (ArrayList<LinkedList<String>> listofLists, ArrayList<Integer> listofIndices)
	{
		
		for(int i = 0 ; i < listofLists.size(); i++)
		{
			LinkedList<String> list = listofLists.get(i);
			if(list.size() > listofIndices.get(i))
				return true;
		}
		
		return false;
	}
	public static boolean isValid2 (ArrayList<LinkedList<String>> listofLists, ArrayList<Integer> listofIndices)
	{
		
		for(int i = 0 ; i < listofLists.size(); i++)
		{
			LinkedList<String> list = listofLists.get(i);
			if(list.size() < listofIndices.get(i))
				return false;
		}
		
		return true;
	}
	

}

class CountComparator implements Comparator<TermData>
{

	public int compare(TermData t1, TermData t2) 
	{
		return t1.count - t2.count;

	}
}

