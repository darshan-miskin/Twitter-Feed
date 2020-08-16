package com.gne.twitter.custom;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnPageScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;

    public OnPageScrollListener(LinearLayoutManager layoutManager){
        this.layoutManager=layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if(dy > 0) {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loadNewData();
            }
        }
    }

    public void loadNewData(){

    }
}
