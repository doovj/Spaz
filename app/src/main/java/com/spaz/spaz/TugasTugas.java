package com.spaz.spaz;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by asyaky on 10/10/2016.
 */

public class TugasTugas extends Fragment {

    Context thiscontext;
    View myView;
    private Database database;
    private TextView empty;
    // private SimpleCursorAdapter cursorAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragmenttugas, container, false);
        thiscontext = container.getContext();
        // sets listView in mainActivity to contents of database
        database = new Database(thiscontext);
        final Cursor cursor = database.getAllItems();

        //broadcastManager to wait for AlarmService to finish
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(thiscontext);
        IntentFilter filter = new IntentFilter("REFRESH");
        broadcastManager.registerReceiver(deleteReceiver, filter);


        mRecyclerView = (RecyclerView) myView.findViewById(R.id.reminderList);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new Adapter(thiscontext, cursor, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

        empty = (TextView) myView.findViewById(R.id.empty);
        emptyCheck();

        adapter.notifyDataSetChanged();


        FloatingActionMenu floatingActionMenu = (FloatingActionMenu) myView.findViewById(R.id.floatingMenu);
        floatingActionMenu.setClosedOnTouchOutside(true);
        FloatingActionButton addAlert = (FloatingActionButton) myView.findViewById(R.id.add_alert);
        FloatingActionButton addNote = (FloatingActionButton) myView.findViewById(R.id.add_note);

        addAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), createOrEditAlert.class));
            }
        });
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), createOrEditNote.class));
            }
        });
        return myView;
    }
    // checks if RecyclerView is empty and sets emptyView
    private void emptyCheck() {
        if (database.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    //receives signal of deletion and then refreshes UI
    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("REFRESH")) {
                emptyCheck();
                adapter.notifyDataSetChanged();
            }
        }
    };
}
