package com.jeffery.holmes.server.view;

/**
 * Interface for view. Can be graph, table or any else.
 */
public interface View {

    String getTitle();

    Type getType();

    enum Type {
        table, graph;
    }

}
