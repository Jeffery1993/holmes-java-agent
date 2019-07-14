package com.jeffery.holmes.server.view;

public interface View {

    String getTitle();

    Type getType();

    enum Type {
        table, graph;
    }

}
