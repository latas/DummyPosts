package com.babylon.testproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.babylon.testproject.R;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.di.components.DaggerPostsComponent;
import com.babylon.testproject.di.modules.PostsModule;
import com.babylon.testproject.presenters.PostsPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsActivity extends BaseActivity implements ListScreen {
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.posts_list)
    RecyclerView recyclerView;

    @Inject
    PostsPresenter presenter;

    @Inject
    PostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_list);
        ButterKnife.bind(this);
        DaggerPostsComponent.builder()
                .appComponent(appComponent()).postsModule(new PostsModule(this, this, position -> presenter.onListItemClicked(position))).build().injectPostsActivity(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        presenter.retrievePosts();
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPosts(List<Post> posts) {
        adapter.clear();
        adapter.showPosts(posts);

    }

    @Override
    public void navigateToDetails(Post post) {


        Intent intent = new Intent(this, PostDetailsActivity.class);
        intent.putExtra(getString(R.string.postIntentKey), post);
        startActivity(intent);

    }

    @Override
    public void showBackButton() {

    }

    @Override
    public void setTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }
}
