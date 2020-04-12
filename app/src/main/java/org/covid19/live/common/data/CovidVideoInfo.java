package org.covid19.live.common.data;

public enum CovidVideoInfo {

    COVID_VIDEO_1("https://www.youtube.com/watch?v=EJbjyo2xa2o",
            "EJbjyo2xa2o",
            "https://img.youtube.com/vi/EJbjyo2xa2o/0.jpg",
            "#COVID-19: Know the right way to wash your hands"),

    COVID_VIDEO_2("https://www.youtube.com/watch?v=OFFg21KhOV0",
            "OFFg21KhOV0",
            "https://img.youtube.com/vi/OFFg21KhOV0/0.jpg",
            "#COVID-19: Watch Mr. Amitabh Bachchan sharing his thoughts on Coronavirus"),

    COVID_VIDEO_3("https://www.youtube.com/watch?v=oBSkHZPu2xU",
            "oBSkHZPu2xU",
            "https://img.youtube.com/vi/oBSkHZPu2xU/0.jpg",
            "#COVID-19: Coronavirus Symptoms");

    private final String videoThumnail;

    private final String videoDescription;

    private final String videoLink;

    private final String videoId;

    CovidVideoInfo(String videoLink, String videoId, String videoThumnail, String videoDescription) {
        this.videoLink = videoLink;
        this.videoId = videoId;
        this.videoThumnail = videoThumnail;
        this.videoDescription = videoDescription;
    }

    public String getVideoThumnail() {
        return videoThumnail;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getVideoId() {
        return videoId;
    }

    public static CovidVideoInfo getDashboardCardViedeo() {
        return COVID_VIDEO_1;
    }


    public static CovidVideoInfo[] getVideoList() {
        return new CovidVideoInfo[]{
                COVID_VIDEO_2,
                COVID_VIDEO_3,
                COVID_VIDEO_1
        };
    }
}
