package org.componenthaus.search;

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.componenthaus.util.file.MockFile;

import java.io.IOException;

public class LuceneSearchServiceTestCase extends TestCase {
    private static final String componentId = "334545";
    private static final String componentDescription = "Big, lumpy and smelly";
    private LuceneSearchService searchService = null;
    private MockLuceneObjectFactory mockLuceneObjectFactory = null;

    protected void setUp() throws Exception {
        mockLuceneObjectFactory = new MockLuceneObjectFactory();
        searchService = new LuceneSearchService(mockLuceneObjectFactory);
    }

    public void testThrowsWrappedExceptionOnIOException() {
        final IOException preparedIoException = new IOException();
        mockLuceneObjectFactory.setPreparedIOException(preparedIoException);

        SearchService.Exception expected = null;
        try {
            searchService.index(componentId,componentDescription);
            fail("Did not get expected exception");
        } catch (SearchService.Exception e) {
            expected = e;
        }
        assertSame(preparedIoException, expected.getCause());
    }

    public void testCallsLuceneIndexApiCorrectly() throws IOException, SearchService.Exception {
        final MockIndexWriter mockIndexWriter = new MockIndexWriter("who cares", new StandardAnalyzer(), true);
        mockIndexWriter.setupExpectedNumOptimizeCalls(1);
        mockIndexWriter.setupExpectedNumCloseCalls(1);
        mockIndexWriter.setupExpectedNumAddDocumentCalls(1);
        mockLuceneObjectFactory.setupPreparedIndexWriter(mockIndexWriter);
        searchService.index(componentId, componentDescription);
        final Document addedDocument = mockIndexWriter.getAddedDocument();
        Field field = addedDocument.getField(LuceneSearchService.ID_FIELD_NAME);
        assertEquals(componentId,field.stringValue());
        field = addedDocument.getField(LuceneSearchService.CONTENTS_FIELD_NAME);
        assertEquals(componentDescription,field.stringValue());
        mockIndexWriter.verify();
    }


    private static final class MockLuceneObjectFactory implements LuceneSearchService.LuceneObjectFactory {
        private IOException preparedIOException = null;
        private MockIndexWriter preparedIndexWriter = null;

        public IndexWriter createIndexWriter(String indexFilePath, Analyzer analyzer, boolean createIndex) throws IOException {
            if ( preparedIOException != null ) {
                throw preparedIOException;
            }
            return preparedIndexWriter;
        }

        public void setPreparedIOException(IOException preparedIOException) {
            this.preparedIOException = preparedIOException;
        }

        public void setupPreparedIndexWriter(MockIndexWriter mockIndexWriter) {
            this.preparedIndexWriter = mockIndexWriter;
        }
    }


    private static final class MockIndexWriter extends IndexWriter {
        private int expectedNumOptimizeCalls = -1;
        private int expectedNumCloseCalls = -1;
        private int expectedNumAddDocumentCalls = -1;
        private int actualNumCloseCalls = 0;
        private int actualNumOptimizeCalls = 0;
        private int actualNumAddDocumentCalls = 0;
        private Document addedDocument = null;

        public MockIndexWriter(String s, Analyzer analyzer, boolean b) throws IOException {
            super(new MockDirectory(),analyzer,true);
        }

        public void setupExpectedNumOptimizeCalls(int i) {
            this.expectedNumOptimizeCalls = i;
        }

        public void setupExpectedNumCloseCalls(int i) {
            this.expectedNumCloseCalls = i;
        }

        public void setupExpectedNumAddDocumentCalls(int i) {
            this.expectedNumAddDocumentCalls = i;
        }

        public synchronized void close() throws IOException {
            this.actualNumCloseCalls++;
        }

        public void addDocument(Document doc) throws IOException {
            this.actualNumAddDocumentCalls++;
            this.addedDocument = doc;
        }

        public synchronized void optimize() throws IOException {
            this.actualNumOptimizeCalls++;
        }

        public void verify() {
            assertEquals(expectedNumAddDocumentCalls,actualNumAddDocumentCalls);
            assertEquals(expectedNumCloseCalls,actualNumCloseCalls);
            assertEquals(expectedNumOptimizeCalls,actualNumOptimizeCalls);
        }

        public Document getAddedDocument() {
            return addedDocument;
        }
    }

    private static final class MockDirectory extends Directory {
        public String[] list()
                throws IOException {
            return new String[0];
        }

        public boolean fileExists(String name)
                throws IOException {
            return false;
        }

        public long fileModified(String name)
                throws IOException {
            return 0;
        }

        public void touchFile(String name)
                throws IOException {
        }

        public void deleteFile(String name)
                throws IOException {
        }

        public void renameFile(String from, String to)
                throws IOException {
        }

        public long fileLength(String name)
                throws IOException {
            return 0;
        }

        public OutputStream createFile(String name)
                throws IOException {
            return new OutputStream() {
                protected void flushBuffer(byte[] b, int len) throws IOException {
                }

                public long length() throws IOException {
                    return 0;
                }
            };
        }

        public InputStream openFile(String name)
                throws IOException {
            return null;
        }

        public Lock makeLock(String name) {
            return new MockLock();
        }

        public void close()
                throws IOException {
        }
    }

    private static final class MockLock extends Lock {
        public boolean obtain() throws IOException {
            return true;
        }

        public void release() {
        }



        public boolean isLocked() {
            return false;
        }
    }
}
