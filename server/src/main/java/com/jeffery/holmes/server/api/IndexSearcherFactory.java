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

    /**
     * Get the {@link IndexSearcher}.
     *
     * @return index searcher
     */
    public static IndexSearcher getIndexSearcher() {
        IndexSearcher indexSearcher;
        try {
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(PathConsts.DATA_DIR)));
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to init IndexReader", e);
        }
        return indexSearcher;
    }

}
