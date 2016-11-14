package com.pperotti.android.one2one.home.presenter;

import com.pperotti.android.one2one.home.view.HomeView;

/**
 * Created by pperotti on 12/6/16.
 */
public class HomePresenterImpl implements HomePresenter {

    HomeView mHomeView;

    public HomePresenterImpl(HomeView homeView) {
        mHomeView = homeView;
    }

    @Override
    public void newTask() {
        mHomeView.onNewTask();
    }
}
