package com.anypli.megamall.presenters;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.anypli.megamall.contracts.BoutiqueContract.BoutiqueViewItf;
import com.anypli.megamall.contracts.BoutiqueContract.BoutiquePresenterItf;
import com.anypli.megamall.models.BackEndInteractor;

public class BoutiquePresenterImpl implements BoutiquePresenterItf {
    private BoutiqueViewItf mViewItf ;

    public BoutiquePresenterImpl(@NonNull BoutiqueViewItf viewItf){
        this.mViewItf=viewItf ;
    }
    @Override
    public void GetBoutiqueData(String id) {
        BackEndInteractor.getInstance().GetBoutiqueData(this,id);
    }

    @Override
    public void GetBoutiqueArticles(String id) {
        BackEndInteractor.getInstance().getBoutiqueArticleList(this,id);
    }

    @Override
    public void ShowOrHideConfigButtons(String boutiqueid) {

        if( BackEndInteractor.getInstance().isCurrentUserBoutique(boutiqueid)){
            mViewItf.showBoutiqueConfigButton(true);
        }else{
            mViewItf.showBoutiqueConfigButton(false);
        }

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
