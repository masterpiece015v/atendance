package com.websarva.wings.android.atendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.websarva.wings.android.atendanceapp.R;

import mysqlite.Database;
import mysqlite.SQLiteAdapter;

public class SubActivity extends AppCompatActivity {
    private TextView txtRoom;
    private TextView txtDate;
    private TextView txtLessonTime;
    private TextView txtSubject;
    private ViewGroup tblGakusei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //ビューを関連付ける
        txtRoom = findViewById(R.id.txtRoom);
        txtDate = findViewById(R.id.txtDate);
        txtLessonTime = findViewById(R.id.txtLessonTime);
        txtSubject = findViewById(R.id.txtSubject);

        //インテントから値を取得する
        Intent intent = getIntent();
        String strRoom = intent.getStringExtra("txtRoom");
        String strDate = intent.getStringExtra("txtDate");
        String strLessonTime = intent.getStringExtra("txtLessonTime");
        String strSubject = intent.getStringExtra("txtSubject");

        //ビューに表示
        txtRoom.setText( strRoom );
        txtDate.setText( strDate );
        txtLessonTime.setText( strLessonTime );
        txtSubject.setText( strSubject );

        //テーブルに表示
        SQLiteAdapter sqlAdapter = new SQLiteAdapter(getApplicationContext(),new Database("mydb.db"));

        String[] g_list = sqlAdapter.getSpinnerString("select distinct g_name from gakusei");

        tblGakusei = (ViewGroup)findViewById(R.id.tblGakusei);
        for(int i = 0 ; i < g_list.length ; i++ ){
            getLayoutInflater().inflate(R.layout.table_row,tblGakusei);
            TableRow tr = (TableRow)tblGakusei.getChildAt(i);
            ((TextView)(tr.getChildAt(0))).setText(g_list[i]);
        }


        Log.d( "g_list",g_list[0] );

    }
}
