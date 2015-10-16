package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
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
		/*a.add("Shr");
		a.add("ct");
		a.add("qtr");
		a.add("October");
		
		a.add("Qtly");*/
		
		a.add("\"i");
		a.add("\"it");
		a.add("\"the");
		
		
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
			index.getTerm(term);
			TermData td = index.getTermData(term);
			
			
			
			if(td == null)
			{
				// Add logic to print NULL;
				
			}
			else
			{
			
				list.add(td);
			}
			
		}
		
	//	TaatOr(list,outputFile);
	//	TaatAnd(list,outputFile);
		DaatOr(list,outputFile);
		
		
		
		
	}
	
	public static void TaatOr(ArrayList<TermData> termDatalist, String outputFile)
	{
		ArrayList<TermData> list = new ArrayList<TermData>(termDatalist);
		int comparisions = 0;
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
			/*for(int i = 0 ; i < list.size(); i++)
			{
				System.out.println("initially count : " + list.get(i).count + "size : " + list.get(i).postingsListSortedByFrequency.size());
				
				
			}*/
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
				/*for(int i = 0 ; i < tempList.size(); i++)
						System.out.println(tempList.get(i).docId);
				System.out.println("comparisions = " + comparisions);*/
					
				//print the comparisions used, documents found (tempList.size()) and seconds used
				// to the output file.
				
				
			}
			
			// now optimized
			Collections.sort(list, new CountComparator());
		/*	for(int i = 0 ; i < list.size(); i++)
			{
				System.out.println("count : " + list.get(i).count + "size :" + list.get(i).postingsListSortedByFrequency.size());
				
				
			}*/
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
				/*for(int i = 0 ; i < tempList1.size(); i++)
						System.out.println(tempList1.get(i).docId);
				System.out.println("comparisions new = " + comparisions);*/

				// print optimal comparisions and final result here.
			}
		
		}
		
	}
	
	public static void TaatAnd(ArrayList<TermData> termDatalist, String outputFile)
	{
		ArrayList<TermData> list = new ArrayList<TermData>(termDatalist);
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
			LinkedList<Posting> tempList = new LinkedList<Posting>(list.get(0).postingsListSortedByFrequency);
			int comparisions = 0;
			for(int i = 1 ; i < list.size(); i++)
			{
				LinkedList<Posting> curList = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> newTemp = new LinkedList<Posting>();
				for(int cur = 1; cur < curList.size(); cur++)
				{
					String curDoc = curList.get(cur).docId;
					for(int p = 0 ; p < tempList.size(); p++)
					{
						comparisions ++;	
						String tempDoc = tempList.get(p).docId;
						
						if(curDoc.equals(tempDoc))
							newTemp.add(curList.get(cur));
						
					}
			
				}
				
				tempList.clear();
				tempList.addAll(newTemp);
				
			}
			
			{
				// print to output file here.
				Collections.sort(tempList, new docIdComparator());
				for(int i = 0 ; i < tempList.size(); i++)
					System.out.println(tempList.get(i).docId);
				System.out.println("comparisions = " + comparisions);
			}
			
			// now optimized
			Collections.sort(list, new CountComparator());
			comparisions = 0;
			
			LinkedList<Posting> tempList1 = new LinkedList<Posting>(list.get(0).postingsListSortedByFrequency);
			
			for(int i = 1 ; i < list.size(); i++)
			{
				LinkedList<Posting> curList1 = list.get(i).postingsListSortedByFrequency;
				LinkedList<Posting> newTemp1 = new LinkedList<Posting>();
				for(int cur = 1; cur < curList1.size(); cur++)
				{
					String curDoc = curList1.get(cur).docId;
					for(int p = 0 ; p < tempList1.size(); p++)
					{
						comparisions ++;	
						String tempDoc = tempList1.get(p).docId;
						
						if(curDoc.equals(tempDoc))
							newTemp1.add(curList1.get(cur));
						
					}
			
				}
				
				tempList1.clear();
				tempList1.addAll(newTemp1);
				
			}
			
			{
				// print to output file here.
				Collections.sort(tempList1, new docIdComparator());
				for(int i = 0 ; i < tempList1.size(); i++)
					System.out.println(tempList1.get(i).docId);
				System.out.println("comparisions new = " + comparisions);
			}
			
			
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
	public static void DaatOr(ArrayList<TermData> list, String outputFile)
	{
		ArrayList<String> result = new ArrayList<String>();
		if(list.size() == 0)
		{
			// print appropriate message in log file
		}
		else
		{
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
					for(int i = 0 ; i < listOfIndices.size(); i++)
					{
						if(i == minIndex)
							listOfIndices.set(i,listOfIndices.get(i) + 1);
					}
				}
			
				
			}
		
		}
		
		System.out.println("");
		
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
	
	

}

class CountComparator implements Comparator<TermData>
{

	public int compare(TermData t1, TermData t2) 
	{
		return t1.count - t2.count;

	}
}

