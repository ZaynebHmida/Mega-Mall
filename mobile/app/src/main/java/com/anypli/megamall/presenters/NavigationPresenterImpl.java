package com.anypli.megamall.presenters;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.anypli.megamall.contracts.NavigationContract.NavigationViewItf;
import com.anypli.megamall.contracts.NavigationContract.NavigationPresenterItf;
import com.anypli.megamall.models.BackEndInteractor;


public class NavigationPresenterImpl implements NavigationPresenterItf {
    private NavigationViewItf mViewItf ;

    public NavigationPresenterImpl(@NonNull NavigationViewItf mViewItf) {
        this.mViewItf = mViewItf;
    }

    @Override
    public void GetNewFeed() {
       BackEndInteractor.getInstance().getNews(this);
    }

    @Override
    public void GetSearchResult(String searchKeywords) {
        BackEndInteractor.getInstance().GetSearchResult(this,searchKeywords);
    }


    @Override
    public void GetAccountData() {
        mViewItf.updateNavigationMenuHeader(BackEndInteractor.getInstance().getAccountData());
    }

    @Override
    public void ShowSearchResult(Bundle[] res) {
        mViewItf.showSearchResult(res);
    }

    @Override
    public void ShowNewFeed(Bundle[] news) {
        mViewItf.showItemList(news);
    }

    @Override
    public void LogOut() {
        BackEndInteractor.getInstance().SignOut();
    }
}
