package com.zyneonstudios.nexus.application.api.discover.search.zyndex;

import com.zyneonstudios.nexus.application.api.discover.search.Search;
import com.zyneonstudios.nexus.application.api.discover.search.SearchResult;
import com.zyneonstudios.nexus.index.ReadableZyndex;
import com.zyneonstudios.nexus.instance.ReadableZynstance;
import java.util.ArrayList;
import java.util.UUID;

public class ZyndexSearch extends Search {

    private ReadableZyndex zyndex;

    public ZyndexSearch(ReadableZyndex readableZyndex) {
        super("Zyndex ("+readableZyndex.getName()+")",UUID.randomUUID());
        this.zyndex = readableZyndex;
    }

    public ReadableZyndex getZyndex() {
        return zyndex;
    }

    public void setZyndex(ReadableZyndex zyndex) {
        this.zyndex = zyndex;
    }

    public void refreshZyndex() {
        String url = this.zyndex.getUrl();
        this.zyndex = null;
        System.gc();

        this.zyndex = new ReadableZyndex(url);
    }

    @Override
    public SearchResult[] getResults() {
        if (zyndex == null || zyndex.getInstances().isEmpty()) {
            return new SearchResult[0];
        }

        ArrayList<SearchResult> results = new ArrayList<>();
        String query = "";
        if(getQuery()!=null) {
            query = getQuery().toLowerCase();
        }

        int i = 0;
        for (ReadableZynstance instance : zyndex.getInstances()) {
            if (i < getLimit()) {
                if(instance.isHidden()) {
                    if(!query.equals(instance.getId().toLowerCase())) {
                        continue;
                    }
                }
                if(instance.getName().toLowerCase().contains(query)||instance.getId().toLowerCase().contains(query)) {
                    SearchResult result = new SearchResult(instance.getId(), instance.getName(), instance.getAuthors().toArray(new String[0]), instance.getSummary(), instance.getThumbnailUrl(), instance.getDownloadUrl(), null);
                    results.add(result);
                    i++;
                }
            } else {
                break;
            }
        }
        return results.toArray(new SearchResult[0]);
    }
}