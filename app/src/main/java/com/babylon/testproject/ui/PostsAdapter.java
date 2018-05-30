package com.babylon.testproject.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babylon.testproject.R;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.models.User;
import com.babylon.testproject.presenters.UserPresenter;
import com.babylon.testproject.ui.listeners.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    @Inject
    LayoutInflater inflater;
    @Inject
    Picasso picasso;
    @Inject
    Context context;
    @Inject
    UserPresenter mPresenter;
    @Inject
    RecyclerItemClickListener listener;

    private List<Post> postsList = new ArrayList<>();

    @Inject
    public PostsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_layout, parent, false);

        return new ViewHolder(itemView);
    }

    public void showPosts(List<Post> posts) {

        this.postsList = posts;
        notifyDataSetChanged();
    }

    public void clear() {
        this.postsList = Collections.emptyList();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Post post = postsList.get(position);
        holder.postText.setText(post.getText());
        holder.userId = postsList.get(position).userId;


        mPresenter.loadUser(post.userId(), new UserDisplayer() {

            @Override
            public void showInfoForUser(User user) {
                if (user.id == holder.userId)
                    picasso.load(context.getString(R.string.avatars_base_url) + user.mail()).into(holder.postIcon);
            }


            @Override
            public void onUserLoadFailed(long userId) {
                if (userId == holder.userId)
                    picasso.load(R.drawable.ic_face).into(holder.postIcon);
            }

            @Override
            public void showUserLoading() {
                picasso.load(R.drawable.ic_face).into(holder.postIcon);
            }
        });
        holder.cellParent.setOnClickListener(view -> listener.onItemClicked(position));


    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_icon)
        ImageView postIcon;
        @BindView(R.id.post_text)
        TextView postText;
        @BindView(R.id.cell_parent)
        LinearLayout cellParent;
        long userId;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}