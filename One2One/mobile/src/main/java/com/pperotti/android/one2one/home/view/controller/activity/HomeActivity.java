package com.pperotti.android.one2one.home.view.controller.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.base.ClickListener;
import com.pperotti.android.one2one.base.ItemDivider;
import com.pperotti.android.one2one.base.RecyclerTouchListener;
import com.pperotti.android.one2one.home.presenter.HomePresenter;
import com.pperotti.android.one2one.home.presenter.HomePresenterImpl;
import com.pperotti.android.one2one.home.view.HomeView;
import com.pperotti.android.one2one.home.view.adapter.HomeAdapter;
import com.pperotti.android.one2one.model.Task;
import com.pperotti.android.one2one.navigator.Navigator;
import com.pperotti.android.one2one.navigator.NavigatorImpl;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    //Widgets
    RecyclerView recyclerView;
    FloatingActionButton fab;

    //Adapter
    HomeAdapter adapter;

    //Presenter
    HomePresenter presenter;

    //Navigator
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDependencies();

        obtainWidgets();

        initializeWidgets(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        loadExistingData();
    }

    private void createDependencies() {
        presenter = new HomePresenterImpl(this);
        navigator = new NavigatorImpl();
    }

    private void obtainWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.home_entryList);
        fab = (FloatingActionButton) findViewById(R.id.home_fab);
    }

    private void initializeWidgets(Bundle savedInstanceState) {

        adapter = new HomeAdapter(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.addItemDecoration(new ItemDivider((int) getResources().getDimension(R.dimen.activity_horizontal_margin)));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.newTask();
            }
        });
    }

    private void loadExistingData() {
        List<Task> tasks = Task.listAll(Task.class);
        adapter.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNewTask() {
        navigator.openNewTask(this);
    }
}
