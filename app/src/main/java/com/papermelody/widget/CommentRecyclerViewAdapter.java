package com.papermelody.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papermelody.R;
import com.papermelody.activity.OnlineListenActivity;
import com.papermelody.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HgS_1217_ on 2017/5/22.
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    /**
     * 用于网络作品显示评论的RecyclerView的Adapter
     */


    private LayoutInflater layoutInflater;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<Comment> comments;

    public CommentRecyclerViewAdapter(Context context) {
        this.context = context;
        comments = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    public CommentRecyclerViewAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = new ArrayList<>(comments);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setView(comments.get(position));
        holder.setContext(context);
        holder.itemView.setOnClickListener((View view) -> {
            int pos = holder.getLayoutPosition();
            onItemClickListener.OnItemClick();
            // TODO:
        });
        holder.setReply(context, comments.get(position));
    }

    @Override
    public int getItemCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        /* 持有每个item的所有界面元素 */

        @BindView(R.id.item_this_comment_context)
        TextView textComment;
        @BindView(R.id.item_this_user_name)
        TextView textUserName;
        @BindView(R.id.item_this_comment_time)
        TextView textCommentTime;
        @BindView(R.id.reply_this_comment)
        LinearLayout replyButton;

        Context contextViewH = null;
        //@BindView User ICON


        public ViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setContext(Context context) {
            contextViewH = context;
        }

        private void setView(Comment comment) {
            textComment.setText(comment.getContent());
            Date date = new Date();
            date.setTime(Long.parseLong(comment.getCreateTime()));
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            textCommentTime.setText(sDateFormat.format(date));
            textUserName.setText(comment.getAuthor());
        }

        public void setReply(Context contextViewH, Comment commentToThisGuy) {
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnlineListenActivity) contextViewH).focusOnEdit(commentToThisGuy);
                    //ToastUtil.showShort("REPLY to " + name);
                }
            });
        }
    }
}
