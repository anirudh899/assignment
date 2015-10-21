package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Indexer 
{
	// have an index variable
	ArrayList<Term> termList;
	heap topKTerms;
	HashMap<String,TermData> map;
	
	public Indexer(String file_name, int top_K)
	{
		termList = new ArrayList<Term>();
		topKTerms = new heap(top_K);
		map = new HashMap<String,TermData>(); // hashmap to store my terms and corresponding term information.
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
				Term t = new Term(line); // constructor here will create the docId sorted list and TF sorted list for this term
				termList.add(t);
				topKTerms.add(t); // adding this term to get topK, using a min heap approach to get the topK terms.
			}
			topKTerms.sort(); // sorting the topK terms.
			createMap(); // building the final hashmap of term string as key and an object of all the term related data as value.
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
	    
	    System.out.println("FUNCTION: getTopK " + top_K);
	    System.out.print("Result: ");
	    printTopK();
	  
	    
	}
	
	public Indexer() 
	{
		// TODO Auto-generated constructor stub
	}

	
	public void print()
	{
		for(int i = 0 ; i < termList.size(); i++)
	    {
	    	Term t = termList.get(i);
	    	System.out.println(t.term_value);
	    	System.out.println(t.count);
	    	for(int j = 0 ; j < t.postingsList.size(); j++)
	    	{
	    		System.out.println(t.postingsList.get(j).docId + " " + t.postingsList.get(j).term_frequency );
	    		
	    	}
	    	System.out.println("###################");
	    }
		
	}
	
	public void createMap()
	{
		for(int i = 0 ; i < termList.size(); i++)
		{
			Term t = termList.get(i);
			TermData td = new TermData(t);
			map.put(t.term_value, td);
		}
	}
	
	public heapTerm[] getTopK()
	{
		return topKTerms.getList();
		
	}
	public void printTopK()
	{
		topKTerms.print();
	}

	public void getTerm(String s) 
	{
		TermData d = map.get(s);
		System.out.println(s);
		System.out.println("count : " + d.count );
		System.out.println(d.postingsListSortedByDoc);
		System.out.println("######");
		for(int i = 0 ; i < d.postingsListSortedByFrequency.size(); i++)
		{
			System.out.print(d.postingsListSortedByFrequency.get(i).docId + " ");
		}
		
		System.out.println();
		
	}
	
	public TermData getTermData(String s)
	{
		TermData d = map.get(s);	
		return d;
	}
		
}



