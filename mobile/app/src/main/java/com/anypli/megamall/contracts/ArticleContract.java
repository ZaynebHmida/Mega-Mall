package com.anypli.megamall.contracts;

import android.os.Bundle;

import com.anypli.megamall.models.AccountData;
import com.anypli.megamall.models.ArticleReview;

public class ArticleContract {
    public interface ArticleViewItf extends CommonContract.ViewItf {
        void showArticlePage(Bundle article ) ;
        void showArticleReviews(Bundle[] usersReview);
        void showArticleConfigButton(boolean show);
    }
    public interface ArticlePresenterItf extends CommonContract.PresenterItf{
        void getArticlePageDetails(String id);
        void getArticlePageReviews(String id);
        void showOrHideConfigButtons( String boutiqueid);
        void submitComment(String articleid,ArticleReview review);
        AccountData getMyAccountData();
    }
}
