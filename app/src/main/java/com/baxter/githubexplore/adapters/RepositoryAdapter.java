package com.baxter.githubexplore.adapters;

/**
 * Created by tbaxter on 1/21/16.
 */


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baxter.githubexplore.R;
import com.baxter.githubexplore.models.Repository;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tbaxter on 1/21/15.
 */
public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {
    private List<Repository> mRepositories;
    private Context mContext;
    private OnItemSelectedListener mOnItemSelectedListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llRepo) LinearLayout llRepo;
        @Bind(R.id.cvRepo) CardView cvRepo;
        @Bind(R.id.tvRepoName) TextView tvRepoName;
        @Bind(R.id.tvRepoDescription) TextView tvRepoDescription;
        @Bind(R.id.tvLanguage) TextView tvLanguage;
        @Bind(R.id.tvForks) TextView tvForks;
        @Bind(R.id.tvStars) TextView tvStars;
        @Bind(R.id.ivFork) ImageView ivFork;
        @Bind(R.id.ivStar) ImageView ivStar;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public RepositoryAdapter(Context context, List<Repository> myRepositories) {
        mContext = context;
        mRepositories = myRepositories;
        if(mRepositories == null) {
            mRepositories = new ArrayList<>();
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }

    public void setRepos(List<Repository> repositories) {
        this.mRepositories = repositories;
        notifyDataSetChanged();
    }

    public void add(int position, Repository item) {
        mRepositories.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Repository item) {
        int position = mRepositories.indexOf(item);
        mRepositories.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RepositoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_repo, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Repository repository = mRepositories.get(position);

        holder.tvRepoName.setText(repository.name);
        holder.tvRepoDescription.setText(repository.description);
        holder.tvForks.setText(String.valueOf(repository.forksCount));
        holder.tvStars.setText(String.valueOf(repository.stargazersCount));
        holder.tvLanguage.setText(repository.language);

        // Add click event to repository item
        holder.cvRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(repository.url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRepositories != null ? mRepositories.size() : 0;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(String url);
    }
}