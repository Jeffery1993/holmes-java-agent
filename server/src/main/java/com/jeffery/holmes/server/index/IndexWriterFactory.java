package com.jeffery.holmes.server.index;

import com.jeffery.holmes.server.consts.PathConsts;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Factory for {@link IndexWriter}.
 */
public class IndexWriterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexWriterFactory.class);
    private static IndexWriter indexWriter;

    static {
        try {
            Directory dir = FSDirectory.open(Paths.get(PathConsts.DATA_DIR));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(dir, indexWriterConfig);
        } catch (IOException e) {
            LOGGER.error("Failed to init IndexWriter due to " + e.getMessage());
        }
    }

    /**
     * Get the {@link IndexWriter}.
     *
     * @return index writer
     */
    public static IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public static void close() {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close IndexWriter due to " + e.getMessage());
            }
        }
    }

}
