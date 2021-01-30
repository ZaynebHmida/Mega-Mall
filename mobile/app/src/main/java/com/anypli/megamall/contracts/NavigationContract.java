package com.anypli.megamall.contracts;

import android.os.Bundle;

import com.anypli.megamall.models.AccountData;

public class NavigationContract {
    public  interface NavigationViewItf {
        void showItemList(Bundle[] in);
        void showSearchResult(Bundle[] res);
        void updateNavigationMenuHeader(AccountData data);
    }

    public interface NavigationPresenterItf{
        void GetNewFeed() ;
        void GetSearchResult (String searchKeywords);
        void GetAccountData();
        void ShowSearchResult(Bundle[] res);
        void ShowNewFeed(Bundle[] news);
        void LogOut() ;
    }
}
