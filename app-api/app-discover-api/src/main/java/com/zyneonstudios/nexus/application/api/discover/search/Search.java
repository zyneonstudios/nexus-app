package com.zyneonstudios.nexus.application.api.discover.search;

import java.util.UUID;

public abstract class Search {

    private boolean lockName = false;
    private boolean lockId = false;
    private String name;
    private String id;
    private String query = null;
    private int offset = 0;
    private int limit = 25;

    public Search(String name, UUID id) {
        this.name = name;
        this.id = id.toString();
    }

    public Search(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Search(String name, UUID id, String query) {
        this.name = name;
        this.id = id.toString();
        this.query = query;
    }

    public Search(String name, String id, String query) {
        this.name = name;
        this.id = id;
        this.query = query;
    }

    public Search(String name, UUID id, String query, int offset) {
        this.name = name;
        this.id = id.toString();
        this.query = query;
        this.offset = offset;
    }

    public Search(String name, String id, String query, int offset) {
        this.name = name;
        this.id = id;
        this.query = query;
        this.offset = offset;
    }

    public Search(String name, UUID id, String query, int offset, int limit) {
        this.name = name;
        this.id = id.toString();
        this.query = query;
        this.offset = offset;
        this.limit = limit;
    }

    public Search(String name, String id, String query, int offset, int limit) {
        this.name = name;
        this.id = id;
        this.query = query;
        this.offset = offset;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setName(String name, boolean lock) {
        if(!lockName) {
            this.name = name;
            lockName = lock;
        }
    }

    public void setId(UUID id, boolean lock) {
        setId(id.toString(),lock);
    }

    public void setId(String id, boolean lock) {
        if(!lockId) {
            this.id = id;
            lockId = lock;
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public abstract SearchResult[] getResults();
}