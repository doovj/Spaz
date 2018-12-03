package com.spaz.spaz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by asyaky on 26/11/2016.
 */

public class createOrEditNote extends AppCompatActivity {
    private EditText title, content;
    private Database database;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_note);
        database = new Database(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);

        content = (EditText) findViewById(R.id.noteContent);
        title = (EditText) findViewById(R.id.noteTitle);

        if (id > 0) {
            Cursor cursor = database.getItem(id);
            cursor.moveToFirst();
            String contentString = cursor.getString(cursor.getColumnIndex(Database.DB_COLUMN_KONTEN));
            String titleString = cursor.getString(cursor.getColumnIndex(Database.DB_COLUMN_NAMA));
            content.setText(contentString);
            title.setText(titleString);
            getSupportActionBar().setTitle("Edit Catatan");
        } else {
            getSupportActionBar().setTitle("Buat Catatan");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        saveNote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_or_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_del_note:
                deleteDialog(id).show();
                break;

            case android.R.id.home:
                saveNote();
                break;

            default:
                break;
        }

        return true;
    }

    private AlertDialog deleteDialog(int id) {

        final int deleteId = id;

        return new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin untuk menghapusnya?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int i) {
                        if (deleteId > 0) {
                            database.deleteItem(deleteId);
                        }
                        terminateActivity();
                    }

                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                })
                .create();

    }

    private AlertDialog saveDialog(int id, String title, String content) {
        final int saveId = id;
        final String saveMessage = content;
        final String saveTitle = title;

        return new AlertDialog.Builder(this)

                .setTitle("Konfirmasi")
                .setMessage("Simpan perubahan?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //if note exists, update. Otherwise insert new note.
                        if (saveId > 0) {
                            database.updateNote(saveId, saveTitle, saveMessage);
                        } else {
                            database.insertNote(saveTitle, saveMessage);
                        }
                        terminateActivity();
                        database.close();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        terminateActivity();
                        database.close();
                        dialog.dismiss();

                    }
                })
                .create();
    }

    private void terminateActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void saveNote() {
        String contentString = content.getText().toString();
        String titleString = title.getText().toString();
        saveDialog(id, titleString, contentString).show();
    }
}