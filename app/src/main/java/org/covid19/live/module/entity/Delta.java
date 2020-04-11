package org.covid19.live.module.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Delta implements Serializable {

    @SerializedName("confirmed")
    private String deltaConfirmed;

    public String getDeltaConfirmed() {
        return deltaConfirmed;
    }

    public void setDeltaConfirmed(String deltaConfirmed) {
        this.deltaConfirmed = deltaConfirmed;
    }
}
