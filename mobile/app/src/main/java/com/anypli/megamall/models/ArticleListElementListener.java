package com.anypli.megamall.models;

import android.content.Intent;
import android.view.View;

import com.anypli.megamall.activities.ArticleActivity;

public class ArticleListElementListener implements View.OnClickListener {
    private String mBoutiqueID ;
    private String mArticleID ;

    public ArticleListElementListener(String boutiqueID, String articleID) {
        this.mBoutiqueID = boutiqueID;
        this.mArticleID = articleID;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(),ArticleActivity.class);
        if(mBoutiqueID!=null)
            intent.putExtra("boutiqueid",mBoutiqueID);
        if(mArticleID!=null)
            intent.putExtra("articleid",mArticleID);
        v.getContext().startActivity(intent);
    }
}
