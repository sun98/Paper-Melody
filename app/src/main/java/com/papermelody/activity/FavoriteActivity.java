package com.papermelody.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.papermelody.R;
import com.papermelody.model.OnlineMusic;
import com.papermelody.model.response.OnlineMusicInfo;
import com.papermelody.model.response.OnlineMusicListResponse;
import com.papermelody.util.App;
import com.papermelody.util.NetworkFailureHandler;
import com.papermelody.util.RetrofitClient;
import com.papermelody.util.SocialSystemAPI;
import com.papermelody.widget.OnlineMusicRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HgS_1217_ on 2017/6/13.
 */

public class FavoriteActivity extends BaseActivity {
    /**
     * 收藏作品列表，根据用户的点赞情况从服务器上获取
     */

    @BindView(R.id.favorite_recycler_view)
    RecyclerView viewFavorite;

    private Context context;
    private OnlineMusicRecyclerViewAdapter adapter;

    private OnlineMusicRecyclerViewAdapter.OnItemClickListener favoriteOnItemClickListener = new
            OnlineMusicRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(OnlineMusic music) {
                    Intent intent = new Intent(context, OnlineListenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OnlineMusic.SERIAL_ONLINEMUSIC, music);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        initGetMusicList();
    }

    private void initGetMusicList() {
        SocialSystemAPI api = RetrofitClient.getSocialSystemAPI();
        addSubscription(api.getFavoriteMusicList(App.getUser().getUserID())
                .flatMap(NetworkFailureHandler.httpFailureFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> ((OnlineMusicListResponse) response).getResult().getMusics())
                .subscribe(
                        musicList -> {
                            List<OnlineMusic> musics = new ArrayList<>();
                            for (OnlineMusicInfo info : musicList) {
                                musics.add(new OnlineMusic(info, context));
                            }
                            initRecyclerView(musics);
                        },
                        NetworkFailureHandler.loginErrorHandler
                ));
    }

    private void initRecyclerView(List<OnlineMusic> musics) {
        adapter = new OnlineMusicRecyclerViewAdapter(context, musics);
        adapter.setOnItemClickListener(favoriteOnItemClickListener);
        viewFavorite.setAdapter(adapter);
        viewFavorite.setLayoutManager(new GridLayoutManager(context, 1));
        viewFavorite.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_favorite;
    }
}
