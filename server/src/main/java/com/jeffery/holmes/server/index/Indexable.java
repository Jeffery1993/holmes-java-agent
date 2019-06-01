package com.jeffery.holmes.server.index;

import org.apache.lucene.document.Document;

/**
 * Interface for class that can be transformed to {@link Document}.
 */
public interface Indexable {

    /**
     * Transform the object to a {@link Document}.
     *
     * @return document
     */
    Document toDocument();

}
