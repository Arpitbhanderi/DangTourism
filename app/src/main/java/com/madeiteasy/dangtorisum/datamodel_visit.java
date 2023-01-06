package com.madeiteasy.dangtorisum;

public class datamodel_visit{
    private String city, image_url,info,map_url,name,website_url, rating;

    private datamodel_visit(){}



    private datamodel_visit(String city, String image_url, String info, String map_url, String name, String website_url, String rating){
        this.city = city;
        this.image_url = image_url;
        this.info = info;
        this.map_url = map_url;
        this.name = name;
        this.website_url = website_url;
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMap_url() {
        return map_url;
    }

    public void setMap_url(String map_url) {
        this.map_url = map_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
