package com.anypli.megamall.contracts;

import android.os.Bundle;

public class ARContract {
    public interface ViewItf{
        void updateBoutiquesMaker(Bundle[] allBoutiques);
    }
    public interface PresenterItf{
        void getAllBoutiques();
        void returnAllBoutiques(Bundle[] allBoutiques);
    }
}
