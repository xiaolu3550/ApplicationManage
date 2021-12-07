package com.xiaolu.applicationmanage.bean;

public class SearchBean {
    private String searchName;
    private int position;

    public SearchBean(String searchName, int position) {
        this.searchName = searchName;
        this.position = position;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
