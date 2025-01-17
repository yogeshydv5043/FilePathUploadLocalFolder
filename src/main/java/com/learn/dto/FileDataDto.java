package com.learn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;



public class FileDataDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    private String name;

    private String banner;


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public FileDataDto() {
    }

}
