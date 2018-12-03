package com.spaz.spaz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Action extends AppCompatActivity {

    private SimpleAdapter adapter;
    private Database database;
    private EditText content, title;
    private String time, date;
    private int id, repeatMode;
    private Map<String, String> item1, item2, item3;
    private DateFormat df, df1;
    private Calendar alertTime;
    private String[] repeatModes;
    private Button sudah,belum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("DELETED");
        broadcastManager.registerReceiver(deleteReceiver, filter);

        sudah = (Button) findViewById(R.id.button);
        belum = (Button) findViewById(R.id.button2);
        sudah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(id).show();
            }
        });
        belum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        database = new Database(this);
        content = (EditText) findViewById(R.id.alertContent);
        title = (EditText) findViewById(R.id.alertTitle);

        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);

        df = new SimpleDateFormat("hh:mm aa");
        df1 = new SimpleDateFormat("dd/MM/yy");

        // If item exists, then set time and date list items to the time and date stored in alert
        if (id > 0) {
            Cursor cursor = database.getItem(id);
            cursor.moveToFirst();
            String contentString = cursor.getString(cursor.getColumnIndex
                    (Database.DB_COLUMN_KONTEN));
            String titleString = cursor.getString(cursor.getColumnIndex(Database.DB_COLUMN_NAMA));
            content.setText(contentString);
            title.setText(titleString);

            getSupportActionBar().setTitle("Spaz");
            cursor.close();

            // Otherwise, set time and date list items to system time
        } else {
            getSupportActionBar().setTitle("Buat Pengingat");

        }
    }

    private AlertDialog deleteDialog(int id) {

        final int deleteId = id;
        return new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin tugas sudah dikerjakan?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {

                        if (deleteId > 0) {
                            // delete the alarm
                            Intent delete = new Intent(Action.this, AlarmService.class);
                            delete.putExtra("id", deleteId);
                            delete.setAction(AlarmService.DELETE);
                            startService(delete);
                        } else {
                            terminateActivity();
                        }
                    }

                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();

    }


    // go back to main activity
    private void terminateActivity() {
        finish();
    }


    // once item is deleted, we can safely exit the activity
    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DELETED")) {
                terminateActivity();
            }
        }
    };

}
