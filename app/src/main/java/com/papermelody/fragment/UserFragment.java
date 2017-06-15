package com.papermelody.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.papermelody.R;
import com.papermelody.activity.FavoriteActivity;
import com.papermelody.activity.HistoryActivity;
import com.papermelody.activity.MainActivity;
import com.papermelody.activity.MessageActivity;
import com.papermelody.activity.UpProductsActivity;
import com.papermelody.model.Message;
import com.papermelody.model.User;
import com.papermelody.model.response.MessageInfo;
import com.papermelody.model.response.MessageResponse;
import com.papermelody.util.App;
import com.papermelody.util.NetworkFailureHandler;
import com.papermelody.util.RetrofitClient;
import com.papermelody.util.SocialSystemAPI;
import com.papermelody.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HgS_1217_ on 2017/4/10.
 */

public class UserFragment extends BaseFragment {
    /**
     * 用例：查看用户信息
     * 用户页面，包括个人信息，已上传作品，收藏作品等等
     */

    @BindView(R.id.text_username)
    TextView textUsername;
    @BindView(R.id.btn_login)
    Button btnLogIn;
    @BindView(R.id.btn_user_info)
    CardView btnUserInfo;
    @BindView(R.id.btn_user_history)
    CardView btnUserHistory;
    @BindView(R.id.btn_user_upload)
    CardView btnUserUpload;
    @BindView(R.id.btn_user_favorite)
    CardView btnUserFavorite;
    @BindView(R.id.btn_message)
    Button btnMessage;

    private User user;
    private Context context;
    private ArrayList<Message> messages;

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();
        initView();

        return view;
    }

    private void initView() {
        updateUser();

        if (App.getUser() != null) {
            getMessage();
        }

        btnLogIn.setOnClickListener((View v) -> {
            if (user == null) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.updateFragment(MainActivity.LOG_IN);
            } else {
                App.setUser(null);
                user = null;
                ToastUtil.showShort(R.string.user_log_out);
                updateUser();
            }
        });

        btnUserInfo.setOnClickListener((View v) -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateFragment(MainActivity.USER_INFO);
        });
        btnUserHistory.setOnClickListener((View v) -> {
            Intent intent = new Intent(context, HistoryActivity.class);
            startActivity(intent);
        });
        btnUserUpload.setOnClickListener((View v) -> {
            if (App.getUser() == null) {
                ToastUtil.showShort(getString(R.string.not_logged_in));
            } else {
                Intent intent = new Intent(context, UpProductsActivity.class);
                startActivity(intent);
            }
        });
        btnUserFavorite.setOnClickListener((View v) -> {
            if (App.getUser() == null) {
                ToastUtil.showShort(getString(R.string.not_logged_in));
            } else {
                Intent intent = new Intent(context, FavoriteActivity.class);
                startActivity(intent);
            }
        });
        btnMessage.setOnClickListener((View v) -> {
            if (App.getUser() == null) {
                ToastUtil.showShort(getString(R.string.not_logged_in));
            } else {
                // TODO: 此处可以搞一个新消息通知图标
                Intent intent = new Intent(context, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Message.SERIAL_MESSAGE, messages);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getMessage() {
        SocialSystemAPI api = RetrofitClient.getSocialSystemAPI();
        addSubscription(api.getMessages(App.getUser().getUserID())
                .flatMap(NetworkFailureHandler.httpFailureFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> ((MessageResponse) response).getResult())
                .subscribe(
                        result -> {
                            // TODO: 消息数量判断

                            List<MessageInfo> infoList = result.getMessages();
                            messages = new ArrayList<>();
                            for (MessageInfo info : infoList) {
                                messages.add(new Message(info));
                            }
                        },
                        NetworkFailureHandler.basicErrorHandler
                ));
    }

    public void updateUser() {
        user = App.getUser();
        if (user == null) {
            Log.d("TEST2", "TEST2");
            textUsername.setText(R.string.un_log_in);
            btnLogIn.setText(R.string.user_log_in);
            btnLogIn.setBackground(getResources().getDrawable(R.drawable.btn_log_in));
        } else {
            Log.d("TEST3", user.getUsername());
            textUsername.setText(user.getUsername());
            btnLogIn.setText(R.string.user_log_out);
            btnLogIn.setBackground(getResources().getDrawable(R.drawable.btn_log_out));
            getMessage();
        }
    }
}
