package org.covid19.live.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class CommonUtiity {

    public static final String TAG = "CommonUtiity";

    /**
     * The following example code will open the Youtube link in the Youtube app
     * if this one is available, otherwise it will open it in a browser:
     *
     * @param context
     * @param videoId
     * @param videoLink
     */
    public static void watchYoutubeVideo(Context context, String videoId, String videoLink) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(videoLink));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

}
