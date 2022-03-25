package com.example.paper.model.movie;

public class StremingProvider {
    private String name;
    private String logo_path;

    public StremingProvider(String name, String logo_path) {
        this.name = name;
        this.logo_path = logo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }
}
