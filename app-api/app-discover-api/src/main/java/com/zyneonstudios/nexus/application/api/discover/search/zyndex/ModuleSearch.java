package com.zyneonstudios.nexus.application.api.discover.search.zyndex;

import com.zyneonstudios.nexus.application.api.discover.search.SearchResult;
import com.zyneonstudios.nexus.index.ReadableZyndex;
import com.zyneonstudios.nexus.modules.ReadableModule;

import java.util.ArrayList;

public class ModuleSearch extends ZyndexSearch {

    public ModuleSearch(ReadableZyndex readableZyndex) {
        super(readableZyndex);
        setName("Modules ("+readableZyndex.getName()+")",false);
    }

    @Override
    public SearchResult[] getResults() {
        if (getZyndex() == null || getZyndex().getModules().isEmpty()) {
            return new SearchResult[0];
        }

        ArrayList<SearchResult> results = new ArrayList<>();
        String query = "";
        if(getQuery()!=null) {
            query = getQuery().toLowerCase();
        }

        int i = 0;
        for (ReadableModule instance : getZyndex().getModules()) {
            if (i < getLimit()) {
                if(instance.isHidden()) {
                    if(!query.equals(instance.getId().toLowerCase())) {
                        continue;
                    }
                }
                if(instance.getName().toLowerCase().contains(query)||instance.getId().toLowerCase().contains(query)) {
                    SearchResult result = new SearchResult(this,instance.getId(), instance.getName(), instance.getAuthors().toArray(new String[0]), instance.getSummary(), instance.getThumbnailUrl(), instance.getDownloadUrl(), null);
                    result.setInstallHandler(new ModuleInstallHandler());
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
