package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Term 
{
	
	String term_value;
	int count;
	ArrayList<Posting> postingsList;
	public Term()
	{
		term_value = "";
		count = -1;
		postingsList = null;
	}
	
	public Term(String s)
	{
		postingsList = new ArrayList<Posting>();
		s.replace("\\","\\\\");
		
		int first_occ = s.indexOf("\\");
		int sec_occ = s.indexOf("\\",first_occ+1);
		String s1 = s.substring(0,first_occ);
		String s2 = s.substring(first_occ+1,sec_occ);
		String s3 = s.substring(sec_occ + 1, s.length());
		
		
		term_value = s1;
		
		StringBuilder sb1 = new StringBuilder(s2);
		sb1.deleteCharAt(0);
		count = Integer.parseInt(sb1.toString());
		
		StringBuilder docs = new StringBuilder(s3);		
		docs.deleteCharAt(0); // delete m
		docs.deleteCharAt(0); // delete [
		docs.deleteCharAt(docs.length() - 1); // delete ]
	
		ArrayList<String> docList =  new ArrayList<String>(Arrays.asList(docs.toString().split(",")));
		for(int i=0; i < docList.size(); i++)
		{
			Posting p = new Posting();
			ArrayList<String> post =  new ArrayList<String>(Arrays.asList(docList.get(i).split("/")));
			p.docId = post.get(0).replaceAll("\\s+","");
			p.term_frequency = Integer.parseInt(post.get(1).replaceAll("\\s+",""));
			postingsList.add(p); // Not sorting now, just blindly adding to the list. But should we sort?
		}
		
		
	}

}

class Posting
{
	String docId;
	int term_frequency;
}
class TermData
{
	LinkedList<String> postingsListSortedByDoc;
	LinkedList<Posting> postingsListSortedByFrequency;
	int count;
	String term;
	
	
	TermData(Term T)
	{
		postingsListSortedByDoc = new LinkedList<String>();
		postingsListSortedByFrequency = new LinkedList<Posting>();
		term = new String(T.term_value);
		
		count = T.count;
		ArrayList<Posting> list = T.postingsList;
		for(int i = 0; i < list.size(); i++)
		{
			postingsListSortedByDoc.add(list.get(i).docId);
			
			postingsListSortedByFrequency.add(list.get(i));
		}
		
		Collections.sort(postingsListSortedByDoc);
		Collections.sort(postingsListSortedByFrequency,new PostingComparator());
		
	}
}

class PostingComparator implements Comparator<Posting>
{

	public int compare(Posting p1, Posting p2) 
	{
		return p2.term_frequency - p1.term_frequency;

	}
}

class docIdComparator implements Comparator<Posting>
{

	public int compare(Posting p1, Posting p2) 
	{
		return p1.docId.compareTo(p2.docId);

	}
}
