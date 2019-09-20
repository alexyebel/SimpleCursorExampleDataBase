package com.example.simplecursorexampledatabase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int CONTACTS_REQUEST_CODE=1;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView contactListView=findViewById(R.id.contacts_list);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        contactListView.setAdapter(adapter);

        if(Build.VERSION.SDK_INT>=23){
            int hasContactsPermission=checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if(hasContactsPermission== PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},CONTACTS_REQUEST_CODE);
            }
            else readContacts();
        }
        else readContacts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CONTACTS_REQUEST_CODE&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            readContacts();
        else
            Toast.makeText(this, "Must give permission", Toast.LENGTH_SHORT).show();
    }

    private void readContacts(){
        //getContentResolver is android dataBase
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        int nameIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int phoneIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        while (cursor.moveToNext()){
            String contact=cursor.getString(nameIndex)+" "+ cursor.getString(phoneIndex);
            adapter.add(contact);
        }
        adapter.notifyDataSetChanged();
    }
}
