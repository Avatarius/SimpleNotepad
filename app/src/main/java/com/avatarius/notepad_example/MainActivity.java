package com.avatarius.notepad_example;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends ListActivity {

    DatabaseHelper helper;
    ListView listViewDb;
    public static final int DELETE_ID = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        registerForContextMenu(getListView());
    }



    private void init() {
        helper = new DatabaseHelper(this);
        helper.open();
        listViewDb = getListView();

        populateListView();



    }

    private void populateListView() {
        Cursor cursor = helper.getAllRecords();

        startManagingCursor(cursor);

        String[] columns = new String[] {DatabaseHelper.KEY_TITLE};
        int[] forUi = new int[] {R.id.noteTitleTextView};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_layout, cursor,
                columns, forUi);
        listViewDb.setAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra(DatabaseHelper.KEY_ID, id);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNoteMenuitem:
                addNote();
                break;

        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo();
                helper.deleteRecord(info.id);
                populateListView();
                return true;

        }

        return super.onContextItemSelected(item);

    }

    private void addNote() {
        Intent i = new Intent(this, NoteActivity.class);
        startActivity(i);

    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
