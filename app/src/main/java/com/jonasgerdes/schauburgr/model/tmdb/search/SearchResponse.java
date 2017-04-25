package com.jonasgerdes.schauburgr.model.tmdb.search;

import java.util.List;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class SearchResponse {
    private int page;
    private int totalPages;
    private int totalResults;
    private List<SearchResult> results;

    public int getPage() {
        return page;
    }

    public SearchResponse setPage(int page) {
        this.page = page;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public SearchResponse setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public SearchResponse setTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public SearchResponse setResults(List<SearchResult> results) {
        this.results = results;
        return this;
    }
}
