package com.anypli.megamall.contracts;

import android.os.Bundle;


public class BoutiqueContract {
    public interface BoutiqueViewItf extends CommonContract.ViewItf {
        void showBoutiqueDetails(Bundle boutique);
        void showBoutiqueArticles(Bundle[] articles);
        void showBoutiqueConfigButton(boolean show);

    }
    public interface BoutiquePresenterItf extends CommonContract.PresenterItf {
        void GetBoutiqueData(String id);
        void GetBoutiqueArticles(String id);
        void ShowOrHideConfigButtons( String boutiqueid);
    }
}
