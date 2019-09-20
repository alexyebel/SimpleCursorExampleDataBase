package com.example.simplecursorexampledatabase;


import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    final int CONTACTS_REQUEST_CODE = 1;
    SimpleCursorAdapter adapter;
    ListView contactsListView;
    Cursor cursor;

    String[] cols = {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};


    final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsListView = findViewById(R.id.contacts_list);

        if(Build.VERSION.SDK_INT>=23) {
            int hasContactsPermission  = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if(hasContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},CONTACTS_REQUEST_CODE);
            }
            else readContacts();
        }
        else readContacts();

        //  startManagingCursor(cursor);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CONTACTS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            readContacts();
        else Toast.makeText(this, "Must give permission", Toast.LENGTH_SHORT).show();
    }

    private void readContacts() {


        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to = {android.R.id.text1,android.R.id.text2};

        // cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,cols,null,null,null);

        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_expandable_list_item_2,null,from,to,0);

        contactsListView.setAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID,null,this);
        //cursor.close();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,ContactsContract.CommonDataKinds.Phone.CONTENT_URI,cols,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(loader.getId() == LOADER_ID) {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //stopManagingCursor(cursor);
    }
}
