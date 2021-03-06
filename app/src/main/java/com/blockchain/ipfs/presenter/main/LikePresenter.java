package com.blockchain.ipfs.presenter.main;

import com.blockchain.ipfs.base.RxPresenter;
import com.blockchain.ipfs.model.DataManager;
import com.blockchain.ipfs.base.contract.main.LikeContract;

import javax.inject.Inject;

/**
 * Created by codeest on 2016/8/23.
 */
public class LikePresenter extends RxPresenter<LikeContract.View> implements LikeContract.Presenter{

    private DataManager mDataManager;

    @Inject
    public LikePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getLikeData() {
        mView.showContent(mDataManager.getLikeList());
    }

    @Override
    public void deleteLikeData(String id) {
        mDataManager.deleteLikeBean(id);
    }

    @Override
    public void changeLikeTime(String id, long time, boolean isPlus) {
        mDataManager.changeLikeTime(id,time,isPlus);
    }

    @Override
    public boolean getLikePoint() {
        return mDataManager.getLikePoint();
    }

    @Override
    public void setLikePoint(boolean b) {
        mDataManager.setLikePoint(b);
    }
}
