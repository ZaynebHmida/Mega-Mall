package com.anypli.megamall.contracts;

import android.os.Bundle;

public class AddEditArticleContract {
    public interface AddEditArticleViewItf extends CommonContract.ViewItf{
        void showMessage(String message);
        void SetPageData(Bundle article);
    }
    public interface AddEditArticlePresenterItf extends CommonContract.PresenterItf {
        void SetArticleData(Bundle article,boolean newArticle);
        void GetArticleData(String articleid);
        void requestDeleteArticle(String mArticleID);
    }
}
