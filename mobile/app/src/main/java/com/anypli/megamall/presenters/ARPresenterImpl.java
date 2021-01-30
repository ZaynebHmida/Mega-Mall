package com.anypli.megamall.presenters;

import android.os.Bundle;

import com.anypli.megamall.contracts.ARContract;
import com.anypli.megamall.models.BackEndInteractor;

import androidx.annotation.NonNull;

public class ARPresenterImpl implements ARContract.PresenterItf {
    ARContract.ViewItf mViewItf ;

    public ARPresenterImpl(@NonNull ARContract.ViewItf viewItf) {
        mViewItf=viewItf ;
    }

    @Override
    public void getAllBoutiques() {
        BackEndInteractor.getInstance().getAllBoutiques(this);
    }

    @Override
    public void returnAllBoutiques(Bundle[] allBoutiques) {
        this.mViewItf.updateBoutiquesMaker(allBoutiques);
    }
}
