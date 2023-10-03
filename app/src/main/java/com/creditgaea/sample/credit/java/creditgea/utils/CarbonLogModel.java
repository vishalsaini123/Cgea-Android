package com.creditgaea.sample.credit.java.creditgea.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 11/27/2016.
 */

public class CarbonLogModel {

    @SerializedName("userId")
    @Expose
    private String user_id;

    @SerializedName("date")
    @Expose
    private String date="0";

    @SerializedName("score")
    @Expose
    private String score="";

    @SerializedName("userName")
    @Expose
    private String user_name="";

    @SerializedName("travelmode")
    @Expose
    private String travel_mode="";

    @SerializedName("traveltype")
    @Expose
    private String travel_type="";

    @SerializedName("traveltime")
    @Expose
    private String travel_time="";


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }



    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getTravel_type() {
        return travel_type;
    }

    public void setTravel_type(String travel_type) {
        this.travel_type = travel_type;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(String travel_time) {
        this.travel_time = travel_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
