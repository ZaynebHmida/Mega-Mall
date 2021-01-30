package com.anypli.megamall.contracts;

import android.os.Bundle;


public class MyBoutiquesContract {
    public interface MyBouttiquesViewItf extends CommonContract.ViewItf{
        void showBoutiquesList(Bundle[] boutiques);
        void showEditBoutiqueActivity(String boutiqueid);
    }
    public interface MyBoutiquesPresenterItf extends CommonContract.PresenterItf{
        void getMyBoutiquesList();

    }
}
