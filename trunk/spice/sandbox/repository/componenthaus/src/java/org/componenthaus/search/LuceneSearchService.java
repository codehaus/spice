package org.componenthaus.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class LuceneSearchService implements SearchService {
    public void index(String componentId, String componentDescription) throws SearchService.Exception {
        IndexWriter writer = null;
        try {
            writer = new IndexWriter("index", new StandardAnalyzer(), true);
            final Document document = new Document();
            document.add(Field.Text("id", componentId));
            document.add(Field.Text("contents", componentDescription));
            writer.addDocument(document);
            writer.optimize();
            writer.close();
        } catch (IOException e) {
            throw new SearchService.Exception("Exception update Lucene index", e);
        }
        System.out.println("Component indexed " + componentId + " with desc " + componentDescription);
    }

    public Collection search(final String query) throws SearchService.Exception {
        final Collection result = new ArrayList();
        try {
            Searcher searcher = new IndexSearcher("index");
            Analyzer analyzer = new StandardAnalyzer();
            Query q = QueryParser.parse(query, "contents", analyzer);
            Hits hits = searcher.search(q);
            System.out.println("Num hits = " + hits.length());
            for(int i=0;i<hits.length();i++) {
                final Document doc = hits.doc(i);
                final String id = doc.get("id");
                System.out.println("Found hit in component id " + id);
                result.add(id);
            }
        } catch (java.lang.Exception e) {
            throw new SearchService.Exception("Exception performing Lucene search",e);
        }
        return result;
    }
}
