package com.baxter.githubexplore.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.baxter.githubexplore.BuildConfig;
import com.baxter.githubexplore.R;
import com.baxter.githubexplore.adapters.RepositoryAdapter;
import com.baxter.githubexplore.models.Repository;
import com.baxter.githubexplore.models.User;
import com.baxter.githubexplore.network.GitHubApi;
import com.baxter.githubexplore.network.GitHubClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment used to search for and show a user's Github information
 */
public class UserFragment extends Fragment {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.ivUser) ImageView mUserImageView;
    @Bind(R.id.rvRepos) RecyclerView mRepos;

    private View mSearchView;
    private GitHubApi mGitHubClient;
    private RepositoryAdapter mAdapter;

    public UserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Inject API key from Gradle build configuration
        String gitHubToken = BuildConfig.GITHUB_API_TOKEN;
        mGitHubClient = GitHubClient.createGitHubClient(gitHubToken);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_user, container, false);

        ButterKnife.bind(this, rootView);

        // Setup toolbar
        if(mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        // Setup repository list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RepositoryAdapter(getActivity(), null);
        mRepos.setAdapter(mAdapter);
        mRepos.setLayoutManager(layoutManager);

        mAdapter.setOnItemSelectedListener(new RepositoryAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String url) {

                // Open repository web page
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }

    private void getUserInfo(final String userName) {

        Call<User> userCall = mGitHubClient.getUser(userName);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {

                if(response.isSuccess()) {

                    // Update UI with user information
                    if(!TextUtils.isEmpty(response.body().avatarUrl)) {
                        Picasso.with(getActivity()).load(response.body().avatarUrl).into(mUserImageView);
                    }
                    if(!TextUtils.isEmpty(response.body().name)) {
                        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        if(actionBar != null) {
                            actionBar.setTitle(response.body().name);
                        }
                    }

                    getUserRepos(userName);
                }
                else {
                    if(response.code() == 404) {
                        Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUserRepos(String userName) {

        final Call<List<Repository>> repoCall = mGitHubClient.getRepos(userName);
        repoCall.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Response<List<Repository>> response) {

                // Update UI with user's repository information
                mAdapter.setRepos(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();            }
        });
    }

    /**
     * Dismiss soft keyboard if it is showing
     */
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);

        MenuItem item = menu.findItem(R.id.search);
        mSearchView = SearchViewCompat.newSearchView(getActivity());
        if (mSearchView != null) {
            SearchViewCompat.setOnQueryTextListener(mSearchView, new SearchViewCompat.OnQueryTextListenerCompat() {
                @Override
                public boolean onQueryTextChange(String newText) {


                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

                    getUserInfo(query);
                    dismissKeyboard();
                    return true;
                }
            });

            MenuItemCompat.setActionView(item, mSearchView);
        }

    }
}
