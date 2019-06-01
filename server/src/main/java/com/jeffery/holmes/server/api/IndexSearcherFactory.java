package com.jeffery.holmes.server.api;

import com.jeffery.holmes.server.consts.PathConsts;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Factory for {@link IndexSearcher}.
 */
public class IndexSearcherFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexSearcherFactory.class);
    private static IndexReader indexReader;
    private static IndexSearcher indexSearcher;

    static {
        try {
            indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(PathConsts.DATA_DIR)));
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            LOGGER.error("Failed to init IndexReader due to " + e.getMessage());
        }
    }

    /**
     * Get the {@link IndexReader}.
     *
     * @return index reader
     */
    public static IndexReader getIndexReader() {
        if (indexReader == null) {
            throw new RuntimeException("IndexReader is null!");
        }
        return indexReader;
    }

    /**
     * Get the {@link IndexSearcher}.
     *
     * @return index searcher
     */
    public static IndexSearcher getIndexSearcher() {
        if (indexSearcher == null) {
            throw new RuntimeException("IndexSearcher is null!");
        }
        return indexSearcher;
    }

    public static void close() {
        if (indexReader != null) {
            try {
                indexReader.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close IndexReader due to " + e.getMessage());
            }
        }
    }

}
