package johnnysc.github.testasyncprocesses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity implements ExecutionThread.CustomCallBack {

    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progres_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        Thread thread = new ExecutionThread(this);
        thread.start();
    }

    @Override
    public void callingBack(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void showToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "video downloaded", Toast.LENGTH_LONG).show();
            }
        });
    }
}