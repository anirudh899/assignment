package code;

import java.util.ArrayList;
import java.util.Arrays;

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


