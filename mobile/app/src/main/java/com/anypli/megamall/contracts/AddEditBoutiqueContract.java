package com.anypli.megamall.contracts;

import android.os.Bundle;

public class AddEditBoutiqueContract {
    public interface AddEditBoutiqueViewItf extends CommonContract.ViewItf{
        void showMessage(String message);
        void SetPageData(Bundle boutique);
    }
    public interface AddEditBoutiquePresenterItf extends CommonContract.PresenterItf {
        void SetBoutiqueData(Bundle boutique);
        void GetBoutiqueData(String boutiqueid);
        void requestDeleteBoutique(String boutiqueID);
    }
}
