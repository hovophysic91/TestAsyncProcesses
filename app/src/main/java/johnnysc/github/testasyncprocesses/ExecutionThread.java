package johnnysc.github.testasyncprocesses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Asatryan on 10.09.17.
 */

public class ExecutionThread extends Thread {

    private final CustomCallBack mCustomCallBack;

    public ExecutionThread(CustomCallBack customCallBack) {
        mCustomCallBack = customCallBack;
    }

    @Override
    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(2);

        Future futurePic = service.submit(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                InputStream stream = new URL("https://www.androidcentral.com/sites/androidcentral.com/files/styles/w550h500/public/wallpapers/batdroid-blj.jpg?itok=9gJ6EVIk").openStream();
                return BitmapFactory.decodeStream(stream);
            }
        });

        Future futureVideo = service.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                InputStream stream = new URL("http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_5mb.mp4").openStream();
                InputStream inputStream = new BufferedInputStream(stream, 8192);
                OutputStream outputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES).getPath()+"/cartoon.mp4");

                byte data[] = new byte[1024];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return null;
            }
        });

        try {
            Bitmap bitmap = (Bitmap) futurePic.get();
            mCustomCallBack.callingBack(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            futureVideo.get();
            mCustomCallBack.showToast();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public interface CustomCallBack {

        void callingBack(Bitmap bitmap);

        void showToast();
    }
}
