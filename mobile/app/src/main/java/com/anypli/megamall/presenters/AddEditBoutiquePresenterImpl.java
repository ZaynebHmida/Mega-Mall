package com.anypli.megamall.presenters;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.anypli.megamall.contracts.AddEditBoutiqueContract.AddEditBoutiqueViewItf;
import com.anypli.megamall.contracts.AddEditBoutiqueContract.AddEditBoutiquePresenterItf;
import com.anypli.megamall.models.BackEndInteractor;

public class AddEditBoutiquePresenterImpl implements AddEditBoutiquePresenterItf {
    private AddEditBoutiqueViewItf mViewItf ;

    public AddEditBoutiquePresenterImpl(@NonNull AddEditBoutiqueViewItf mViewItf) {
        this.mViewItf = mViewItf;
    }

    @Override
    public void SetBoutiqueData(Bundle boutique) {
        BackEndInteractor.getInstance().SetBoutiqueData(this,boutique);
    }

    @Override
    public void GetBoutiqueData(String boutiqueid) {
        BackEndInteractor.getInstance().GetBoutiqueData(this,boutiqueid);
    }

    @Override
    public void requestDeleteBoutique(String boutiqueID) {
        BackEndInteractor.getInstance().deleteBoutique(this,boutiqueID);
    }

    @Override
    public void dealWithIt(Bundle input) {
        mViewItf.dealWithIt(input);
    }

    @Override
    public void dealWithIt(Object input) {
        mViewItf.dealWithIt(input);
    }
}
