package com.learn.dto;

import org.springframework.core.io.Resource;

public class ResourceDto {

    private Resource resource;

    private String type;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public ResourceDto(String type, Resource resource) {
        this.type = type;
        this.resource = resource;
    }

    public ResourceDto() {
    }
}
