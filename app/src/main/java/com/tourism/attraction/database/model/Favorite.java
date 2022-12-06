package com.tourism.attraction.database.model;

public class Favorite {
    private int id;
    private String  user_name;
    private String  attraction_name;

    public Favorite(int id, String user_name, String attraction_name) {
        this.id = id;
        this.user_name = user_name;
        this.attraction_name = attraction_name;
    }

}
