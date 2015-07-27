package com.avatarius.notepad_example;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteActivity extends Activity {

    private EditText editTextNote;
    private DatabaseHelper helper;
    private Long _id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_layout);
        init();
        Bundle extras = getIntent().getExtras();
        if (extras != null) edit();
        }





    private void init() {
        editTextNote = (EditText) findViewById(R.id.editTextNote);
        helper = new DatabaseHelper(this);
        helper.open();

    }

    private void edit() {
        Long noteId = getIntent().getLongExtra(DatabaseHelper.KEY_ID, 0);
        Cursor cursor = helper.getRecord(noteId);
        cursor.moveToFirst();
        System.out.println(cursor.getColumnCount());
        String noteContent = cursor.getString(1);
        editTextNote.setText(noteContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNoteMenuitem:
                saveNote();
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private void saveNote() {
        Bundle extras = getIntent().getExtras();
        String noteText = editTextNote.getText().toString();

        if (extras != null) {
            helper.updateNote(getIntent().getLongExtra(DatabaseHelper.KEY_ID, 1),
                    noteText);
        } else {
            String noteToSave = noteText;
            String noteTitle = "";
            if (noteToSave.length() > 23) {
                noteTitle = noteToSave.substring(0, 23);
            } else {
                noteTitle = noteText;
            }
            helper.addRecord(noteTitle, noteToSave);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}
