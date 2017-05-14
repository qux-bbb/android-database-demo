package com.neo.databasedemo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int ADD_REQUEST = 1;
    private final int UPDATE_REQUEST = 2;


    Button addButton;
    ListView infoListView;

    private SQLiteOpenHelper dbHelper;

    ArrayAdapter<String> adapter;

    List<String> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.add_button);
        infoListView = (ListView) findViewById(R.id.info_listView);

        // 创建Info数据库并创建一张Info表
        dbHelper = new SQLiteOpenHelper(this,"Info.db",null,1) {

            public static final String CREATE_INFO =
                    "create table Info ("
                            + "id integer primary key autoincrement, "
                            + "name text, "
                            + "what text)";

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_INFO);
                Toast.makeText(MainActivity.this, "Table Info Create succeeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("drop table if exists Info");
                onCreate(db);
            }
        };




        // 跳转到添加一项的Activity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,ADD_REQUEST);
            }
        });

        infoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String[] aInfo = ((TextView)view).getText().toString().split("\n");


                final String id0 = aInfo[0];
                final String name =aInfo[1];
                final String what = aInfo[2];

                Log.d("Hello",aInfo[0] + ":" + aInfo[1] + ":" + aInfo[2]);

                // 选择删除或修改操作
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Please select an action")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteInfo(id0);
                            }
                        })
                        .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                                intent.putExtra("id",id0);
                                intent.putExtra("name",name);
                                intent.putExtra("what",what);
                                startActivityForResult(intent,UPDATE_REQUEST);
                            }
                        }).show();


                return true;
            }
        });


    }


    // 在每次MainActivity到前台时都会更新Info列表
    @Override
    protected void onStart() {
        super.onStart();
        queryInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_REQUEST:
                if(resultCode == RESULT_OK){
                    String name = data.getStringExtra("name");
                    String what = data.getStringExtra("what");
                    addInfo(name,what);
                }
                break;
            case UPDATE_REQUEST:
                if(resultCode == RESULT_OK){
                    String id = data.getStringExtra("id");
                    String name = data.getStringExtra("name");
                    String what = data.getStringExtra("what");
                    updateInfo(id,name,what);
                }
                break;
        }

    }



    private void addInfo(String name,String what){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 开始组装 数据
        values.put("name", name);
        values.put("what", what);

        if(db.insert("Info", null, values) != -1){
            queryInfo();
            Toast.makeText(this, "Add Success!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Add Failed!", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteInfo(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(db.delete("Info","id = ?",new String[]{id}) != 0){
            queryInfo();
            Toast.makeText(this, "Delete Success!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Delete Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateInfo(String id, String name, String what) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("what",what);
        
        if(db.update("Info",values,"id = ?", new String[]{id}) > 0){
            queryInfo();
            Toast.makeText(this, "Update Success!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Update Failed!", Toast.LENGTH_SHORT).show();
        }

    }

    // 列出数据库中信息
    private void queryInfo(){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        infoList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,infoList);

        Cursor cursor = db.query("Info",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String what = cursor.getString(cursor.getColumnIndex("what"));

                infoList.add(id + "\n" + name + "\n" + what);
            }while(cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();
        infoListView.setAdapter(adapter);

        cursor.close();
    }


}
