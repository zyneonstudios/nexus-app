package com.zyneonstudios.nexus.application.api.discover.search;

import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.frame.web.ApplicationFrame;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;

import java.util.Arrays;
import java.util.HashMap;

public class DiscoverSearch {

    private final HashMap<String, Search> searchSources = new HashMap<>();
    private Search activeSearchSource = null;
    private final ApplicationFrame frame;

    public DiscoverSearch(ApplicationFrame frame) {
        this.frame = frame;
    }

    public Search[] getSearchSources() {
        return searchSources.values().toArray(new Search[0]);
    }

    public Search getActiveSearchSource() {
        return activeSearchSource;
    }

    public String getActiveSourceId() {
        return activeSearchSource.getId();
    }

    public void setActiveSearchSource(String id) {
        if(searchSources.containsKey(id)) {
            this.activeSearchSource = searchSources.get(id);
        }
    }

    public void setActiveSearchSource(Search searchSource) {
        if(!searchSources.containsValue(searchSource)) {
            if(!searchSources.containsKey(searchSource.getId())) {
                searchSources.put(searchSource.getId(), searchSource);
            } else {
                searchSources.replace(searchSource.getId(),searchSource);
            }
        }
        this.activeSearchSource = searchSource;
    }

    public void addSearchSource(Search searchSource) {
        if(searchSources.containsKey(searchSource.getId())||searchSources.containsValue(searchSource)) {
            searchSources.replace(searchSource.getId(),searchSource);
        } else {
            searchSources.put(searchSource.getId(), searchSource);
        }
    }

    public void removeSearchSource(Search searchSource) {
        searchSources.remove(searchSource.getId());
    }

    public void removeSearchSource(String id) {
        searchSources.remove(id);
    }

    public void open() {
        SharedAPI.openFrameUrl(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html?l=search");
    }

    public void open(String query) {
        SharedAPI.openFrameUrl(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html?l=search&s="+query);
        activeSearchSource.setQuery(query);
    }

    public void open(String query, String searchTypeId) {
        String url = ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html?l=search&moduleId="+searchTypeId;
        if(query!=null&&!query.isEmpty()) {
            url = url+"&s="+query;
            activeSearchSource.setQuery(query);
        }
        SharedAPI.openFrameUrl(url);
    }

    public void addResult(SearchResult result) {
        addResult(result.getId(),result.getName(),result.getSummary(),result.getMetaSummary(),result.getAuthors(),result.getImageUrl());
    }

    public void addResult(String id, String name, String description, String meta, String[] authors, String imageUrl) {
        addResult(id,name,description,meta,Arrays.toString(authors).replace("[","").replace("]",""),imageUrl);
    }

    public void addResult(String id, String name, String description, String meta, String author, String imageUrl) {
        String script = "addResult('"+id+"','"+imageUrl+"',\""+name.replace("\"","''")+"\",\""+author.replace("\"","''")+"\",\""+description.replace("\n","<br>").replace("\"","''")+"\",\""+meta.replace("\n","<br>").replace("\"","''")+"\");";
        frame.executeJavaScript(script);
    }

    public HashMap<String, Search> getSearchSourcesById() {
        return new HashMap<>(searchSources);
    }
}