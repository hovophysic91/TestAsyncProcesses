package johnnysc.github.testasyncprocesses;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import static johnnysc.github.testasyncprocesses.Consts.VIDEO_PATH;

public class MainActivity extends AppCompatActivity implements ExecutionThread.CustomCallBack {

    private VideoView mVideoView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initProgressDialog();
        showProgressDialog();
        getData();
    }

    @Override
    public void showImage(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void showVideo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mProgressDialog.hide();
                mVideoView.setVisibility(View.VISIBLE);
                mVideoView.setVideoURI(Uri.parse(VIDEO_PATH));
                mVideoView.start();
                showToast(R.string.video_downloaded);
            }
        });
    }

    @Override
    public void incrementProgress(final int increment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.incrementProgressBy(increment);
            }
        });
    }

    @Override
    public void showError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(R.string.error);
            }
        });
    }

    private void getData() {
        Thread thread = new ExecutionThread(this);
        thread.start();
    }

    private void initUI() {
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setTitle(R.string.video_downloading);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(5000000);
    }

    private void showProgressDialog() {
        mProgressDialog.show();
    }

    private void showToast(@StringRes int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
    }
}