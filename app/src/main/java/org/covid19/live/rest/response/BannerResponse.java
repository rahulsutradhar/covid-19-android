package org.covid19.live.rest.response;

import com.google.gson.annotations.SerializedName;

import org.covid19.live.module.entity.Banner;

import java.io.Serializable;
import java.util.ArrayList;

public class BannerResponse implements Serializable {

    @SerializedName("factoids")
    ArrayList<Banner> bannerArrayList;

    public ArrayList<Banner> getBannerArrayList() {
        return bannerArrayList;
    }

    public void setBannerArrayList(ArrayList<Banner> bannerArrayList) {
        this.bannerArrayList = bannerArrayList;
    }
}
