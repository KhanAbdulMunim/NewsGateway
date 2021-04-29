package com.abdulmunimkhan.newsgateway;

import java.io.Serializable;

public class NewsSource implements Serializable {
    private String name;
    private String id;
    private String category;

    public NewsSource(String name, String id, String category){
        this.name = name;
        this.id = id;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
