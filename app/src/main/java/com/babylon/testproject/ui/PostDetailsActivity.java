package com.babylon.testproject.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.babylon.testproject.R;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.models.User;
import com.babylon.testproject.di.components.DaggerPostComponent;
import com.babylon.testproject.di.modules.PostModule;
import com.babylon.testproject.presenters.PostDetailsPresenter;
import com.babylon.testproject.presenters.UserPresenter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsActivity extends BaseActivity implements DetailsScreen, UserDisplayer {


    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.post_title)
    TextView postTitle;
    @BindView(R.id.post_body)
    TextView postBody;
    @BindView(R.id.post_username)
    TextView userNameTextView;
    @BindView(R.id.comments_count)
    TextView commentsCountTextView;
    @Inject
    Picasso picasso;
    @Inject
    UserPresenter avatarPresenter;
    @Inject
    PostDetailsPresenter postDetailsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Post post = getIntent().getExtras().getParcelable(getString(R.string.postIntentKey));
        setContentView(R.layout.post_details);
        ButterKnife.bind(this);

        DaggerPostComponent.builder()
                .appComponent(appComponent()).postModule(new PostModule(this, post.id)).build().injectPostActivity(this);

        postTitle.setText(post.title);
        postBody.setText(post.text);
        avatarPresenter.loadUser(post.userId, this);
        postDetailsPresenter.loadComments();
    }


    @Override
    public void showCommentNumbers(long comments) {
        commentsCountTextView.setText(String.valueOf(comments));
    }

    @Override
    public void onCommentsError() {
        commentsCountTextView.setText("N/A");
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showInfoForUser(User user) {
        picasso.load(getString(R.string.avatars_base_url) + user.mail()).into(avatar);
        userNameTextView.setText(user.userName);
    }

    @Override
    public void onUserLoadFailed(long userId) {
        userNameTextView.setText("N/A");
        picasso.load(R.drawable.ic_face).into(avatar);
    }

    @Override
    public void showUserLoading() {
        picasso.load(R.drawable.ic_face).into(avatar);
    }

    @Override
    public void showBackButton() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void setTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }

    }
}
