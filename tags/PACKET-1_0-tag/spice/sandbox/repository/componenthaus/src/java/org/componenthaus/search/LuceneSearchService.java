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
import org.apache.lucene.search.HitCollector;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LuceneSearchService implements SearchService {
    static final String ID_FIELD_NAME = "id";
    static final String CONTENTS_FIELD_NAME = "contents";
    private static final String INDEX_FILE_PATH = "index";

    private final LuceneObjectFactory luceneObjectFactory;

    public LuceneSearchService(LuceneObjectFactory luceneObjectFactory) {
        this.luceneObjectFactory = luceneObjectFactory;
    }

    public void index(String componentId, String componentDescription) throws SearchService.Exception {
        IndexWriter writer = null;
        try {
            writer = luceneObjectFactory.createIndexWriter(INDEX_FILE_PATH, new StandardAnalyzer(), !indexExists());
            final Document document = new Document();
            document.add(Field.Text(ID_FIELD_NAME, componentId));
            document.add(Field.Text(CONTENTS_FIELD_NAME, componentDescription));
            writer.addDocument(document);
            writer.optimize();
            writer.close();
        } catch (IOException e) {
            throw new SearchService.Exception("Exception updating Lucene index", e);
        }
    }

    private boolean indexExists() {
        return new File(INDEX_FILE_PATH).exists();
    }

    //This is tough to unit test as Hits is final, and the constructor is package protected
    public int search(final String query, int beginIndex, final int endIndex, final List collector) throws SearchService.Exception {
        int numHits = 0;
        try {
            final Searcher searcher = new IndexSearcher(INDEX_FILE_PATH);
            final Analyzer analyzer = new StandardAnalyzer();
            final Query q = QueryParser.parse(query, CONTENTS_FIELD_NAME, analyzer);
            final Hits hits = searcher.search(q);
            numHits = hits.length();
            while (beginIndex <= endIndex && beginIndex < numHits) {
                final Document doc = hits.doc(beginIndex++);
                collector.add(doc.get(ID_FIELD_NAME));
            }
        } catch (java.lang.Exception e) {
            throw new SearchService.Exception("Exception performing Lucene search",e);
        }
        return numHits;
    }

    static interface LuceneObjectFactory {
        IndexWriter createIndexWriter(String indexFilePath, Analyzer analyzer, boolean createIndex) throws IOException;
    }

    public static final class DefaultLuceneObjectFactory implements LuceneObjectFactory {
        public IndexWriter createIndexWriter(String indexFilePath, Analyzer analyzer, boolean createIndex) throws IOException {
            return new IndexWriter(indexFilePath,analyzer,createIndex);
        }
    }

}
