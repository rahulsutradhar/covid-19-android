package org.covid19.live.module.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Banner implements Serializable {

    @SerializedName("banner")
    private String bannerTitle;

    @SerializedName("id")
    private String id;

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
