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
		                               "Google", null));
		wqList.add(new WebQuery("pubmed", "PubMed Search", "https://pubmed.ncbi.nlm.nih.gov/?term=",
		                               "PubMed", " OR "));
		wqList.add(new WebQuery("pubmedcentral", "PubMed Central Search", "https://www.ncbi.nlm.nih.gov/pmc/?term=",
		                               "PubMed Central", " OR "));
		wqList.add(new WebQuery("iquery", "iQuery Search", "http://search.ndexbio.org/?genes=",
		                               "iQuery", null));
                wqList.add(new WebQuery("genecard", "GeneCards Search", "https://www.genecards.org/Search/Keyword?queryString=",
		                               "GeneCards", null));
                wqList.add(new WebQuery("gprofilerhsapiens",
                        "gProfiler Homo sapiens", 
                        "https://biit.cs.ut.ee/gprofiler/gost?organism=hsapiens&ordered=false&all_results=false&no_iea=false&combined=false&measure_underrepresentation=false&domain_scope=annotated&significance_threshold_method=g_SCS&user_threshold=0.05&numeric_namespace=ENTREZGENE_ACC&sources=GO:MF,GO:CC,GO:BP,KEGG,TF,REAC,MIRNA,HPA,CORUM,HP,WP&background=&query=",
                        "gProfiler Homo sapiens Search", null));
		wqList.add(new WebQuery("MSigDB", "MSigDB Search", "http://www.gsea-msigdb.org/gsea/analysisApi?speciesName=Human&username=ndex_user&op=annotate&geneIdList=", "MSigDB", ","));
		return wqList;
	}
}
