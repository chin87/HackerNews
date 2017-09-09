package com.altimetric.saurabh.chinmayhackernews.UI;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altimetric.saurabh.chinmayhackernews.BR;
import com.altimetric.saurabh.chinmayhackernews.POJO.StoryResponse;
import com.altimetric.saurabh.chinmayhackernews.R;

import java.util.ArrayList;


/**
 * Created by hp on 9/9/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsListBindingHolder> {

    private Context mContext;
    private ArrayList<StoryResponse> mStoryList;
    private IClick iClick;

    public NewsAdapter(Context context, ArrayList<StoryResponse> storyList, IClick iClick) {
        mContext = context;
        mStoryList = storyList;
        this.iClick = iClick;
    }

    @Override
    public NewsListBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent,
                false);
        return new NewsListBindingHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsListBindingHolder holder, int position) {
        final StoryResponse story = mStoryList.get(position);
        holder.getBinding().setVariable(BR.story, story);
        holder.getBinding().setVariable(BR.callback, this);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    public class NewsListBindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private StoryResponse storyResponse;

        public NewsListBindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCLicked(getStoryResponse());
                }
            });
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        private StoryResponse getStoryResponse() {
            return storyResponse;
        }

        private void setStoryResponse(StoryResponse storyResponse) {
            this.storyResponse = storyResponse;
        }
    }

    public interface IClick {
        public void clickedForUrl(String url);
    }

    public void update(int position, StoryResponse storyResponse) {
        mStoryList.set(position, storyResponse);
        notifyItemChanged(position);
    }

    public void itemCLicked(StoryResponse storyResponse) {
        Log.i("", "" + storyResponse.getUrl());
        if (iClick != null) {
            iClick.clickedForUrl(storyResponse.getUrl());
        }
    }
}
