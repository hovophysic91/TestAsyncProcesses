package johnnysc.github.testasyncprocesses;

import android.os.Environment;

/**
 * @author Asatryan on 16.09.17.
 */

public class Consts {

    public static final String IMAGE_URL = "https://www.androidcentral.com/sites/androidcentral.com/files/styles/w550h500/public/wallpapers/batdroid-blj.jpg?itok=9gJ6EVIk";
    public static final String VIDEO_URL = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_5mb.mp4";
    public static final String VIDEO_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath() + "/cartoon.mp4";

    private Consts() {
        throw new IllegalStateException("can't create object");
    }
}