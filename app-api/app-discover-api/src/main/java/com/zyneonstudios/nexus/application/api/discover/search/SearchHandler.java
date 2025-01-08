package com.zyneonstudios.nexus.application.api.discover.search;

import com.zyneonstudios.nexus.application.api.discover.events.DiscoverSearchEvent;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

public class SearchHandler extends DiscoverSearchEvent {

    @Override
    public boolean onSearch() {
        try {
            DiscoverSearch search = getDiscover().getSearch();

            if(search.getSearchSourcesById().containsKey(getSourceId())) {
                search.setActiveSearchSource(getSourceId());
                Search activeSearch = search.getActiveSearchSource();
                activeSearch.setQuery(getQuery());
                activeSearch.setOffset(getOffset());

                for (SearchResult result : activeSearch.getResults()) {
                    search.addResult(result);
                }

                return true;
            }
        } catch (Exception e) {
            NexusDesktop.getLogger().err("Couldn't resolve search: " + e.getMessage());
        }
        return false;
    }
}