package edu.ucsd.idekerlab.webbymcsearch.query;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author churas
 */
public class WebQueryFactory {
	
	
	public Set<WebQuery> getWebQueries(){
		Set<WebQuery> wqList = new LinkedHashSet<>();
		wqList.add(new WebQuery("google", "Google Search", "https://www.google.com/search?q=",
		                               "Google", false));
		wqList.add(new WebQuery("pubmed", "PubMed Search", "https://pubmed.ncbi.nlm.nih.gov/?term=",
		                               "PubMed", true));
		wqList.add(new WebQuery("pubmedcentral", "PubMed Central Search", "https://www.ncbi.nlm.nih.gov/pmc/?term=",
		                               "PubMed Central", true));
		wqList.add(new WebQuery("iquery", "iQuery Search", "http://search.ndexbio.org/?genes=",
		                               "iQuery", false));
                wqList.add(new WebQuery("genecard", "GeneCards Search", "https://www.genecards.org/Search/Keyword?queryString=",
		                               "GeneCards", false));
		return wqList;
	}
}
