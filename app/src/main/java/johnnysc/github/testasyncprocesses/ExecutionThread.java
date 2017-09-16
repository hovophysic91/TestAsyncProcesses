package johnnysc.github.testasyncprocesses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import static johnnysc.github.testasyncprocesses.Consts.IMAGE_URL;
import static johnnysc.github.testasyncprocesses.Consts.VIDEO_PATH;
import static johnnysc.github.testasyncprocesses.Consts.VIDEO_URL;

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
                InputStream stream = new URL(IMAGE_URL).openStream();
                return BitmapFactory.decodeStream(stream);
            }
        });

        Future futureVideo = service.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                InputStream stream = new URL(VIDEO_URL).openStream();
                InputStream inputStream = new BufferedInputStream(stream, 8192);
                OutputStream outputStream = new FileOutputStream(VIDEO_PATH);

                byte data[] = new byte[1024];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    mCustomCallBack.incrementProgress(count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return null;
            }
        });

        try {
            Bitmap bitmap = (Bitmap) futurePic.get();
            mCustomCallBack.showImage(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            mCustomCallBack.showError();
        }

        try {
            futureVideo.get();
            mCustomCallBack.showVideo();
        } catch (InterruptedException | ExecutionException e) {
            mCustomCallBack.showError();
        }
    }


    public interface CustomCallBack {

        void showImage(Bitmap bitmap);

        void showVideo();

        void incrementProgress(int increment);

        void showError();
    }
}
