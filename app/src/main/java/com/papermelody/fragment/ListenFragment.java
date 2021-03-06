package com.papermelody.fragment;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.papermelody.R;
import com.papermelody.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by HgS_1217_ on 2017/4/10.
 */

public class ListenFragment extends BaseFragment {
    /**
     * 试听页面的试听部分，即播放器部分
     */
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.img_music)
    ImageView imgMusic;

    private static final String FILEPATH = "FILEPATH";
    private static final String IMAGEURL = "IMAGEURL";
    private static MediaPlayer mediaPlayer;
    private TimerTask timerTask;
    private Timer timer;
    private boolean playState;
    private static String filepath;
    private static String imageUrl;

    public static ListenFragment newInstance(String fn, String imgUrl) {
        ListenFragment fragment = new ListenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILEPATH, fn);
        bundle.putString(IMAGEURL, imgUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = new MediaPlayer();
        filepath = getArguments().getString(FILEPATH);
        imageUrl = getArguments().getString(IMAGEURL);
        playState = false;
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.i("nib", filepath);
            ToastUtil.showShort(R.string.unable_to_play);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_listen, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        if (imageUrl != null)
            Picasso.with(getContext()).load(imageUrl).into(imgMusic);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress() * mediaPlayer.getDuration() / 100);
            }
        });
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // FIXME: duration为0时，会崩溃闪退
                if (mediaPlayer.getDuration() != 0) {
                    seekBar.setProgress(100 * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration());
                } else {
                    seekBar.setProgress(0);  // FIXME: 临时处理崩溃情况
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        mediaPlayer.release();
    }

    public void starPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            playState = true;
        }
    }

    public void pausePlay() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            playState = false;
        }
    }

    public void forwardPlay() {
        if (mediaPlayer != null) {
            int targetProgress = (seekBar.getProgress() + 3) <= 100 ? (seekBar.getProgress() + 3) : 100;
            mediaPlayer.seekTo(targetProgress * mediaPlayer.getDuration() / 100);
        }
    }

    public void backwardPlay() {
        if (mediaPlayer != null) {
            int targetProgress = (seekBar.getProgress() - 3) >= 0 ? (seekBar.getProgress() - 3) : 0;
            mediaPlayer.seekTo(targetProgress * mediaPlayer.getDuration() / 100);
        }
    }

    public boolean getPlayState() {
        return playState;
    }
}
