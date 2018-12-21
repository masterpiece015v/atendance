package com.example.watabe.atendance;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mysqlite.Database;
import mysqlite.SQLiteAdapter;
import mysqlite.TableData;
import mysqlite.TableRowData;

public class MakeTimeTableActivity extends AppCompatActivity {
    //フィールド
    private Map<String,TextView> txtMap = new HashMap<>();
    private SQLiteAdapter sqlAdapter;
    private PopupWindow popWin;
    private MakeTimeTableActivity parent;
    private String strRoom;
    private ListAlert dialog;
    private TextView tempTextView;
    //メソッド
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_time_table);

        //
        parent = this;
        //インテント
        Intent intent = getIntent();
        this.strRoom = intent.getStringExtra("txtRoom");
        //クラス名の挿入
        ((TextView)findViewById(R.id.txtRoom)).setText( strRoom );
        //MapにtxtXXXYを登録する
        txtMap.put("MON1",(TextView)findViewById(R.id.txtMON1));
        txtMap.put("MON2",(TextView)findViewById(R.id.txtMON2));
        txtMap.put("MON3",(TextView)findViewById(R.id.txtMON3));
        txtMap.put("MON4",(TextView)findViewById(R.id.txtMON4));

        txtMap.put("TUE1",(TextView)findViewById(R.id.txtTUE1));
        txtMap.put("TUE2",(TextView)findViewById(R.id.txtTUE2));
        txtMap.put("TUE3",(TextView)findViewById(R.id.txtTUE3));
        txtMap.put("TUE4",(TextView)findViewById(R.id.txtTUE4));

        txtMap.put("WED1",(TextView)findViewById(R.id.txtWED1));
        txtMap.put("WED2",(TextView)findViewById(R.id.txtWED2));
        txtMap.put("WED3",(TextView)findViewById(R.id.txtWED3));
        txtMap.put("WED4",(TextView)findViewById(R.id.txtWED4));

        txtMap.put("THU1",(TextView)findViewById(R.id.txtTHU1));
        txtMap.put("THU2",(TextView)findViewById(R.id.txtTHU2));
        txtMap.put("THU3",(TextView)findViewById(R.id.txtTHU3));
        txtMap.put("THU4",(TextView)findViewById(R.id.txtTHU4));

        txtMap.put("FRI1",(TextView)findViewById(R.id.txtFRI1));
        txtMap.put("FRI2",(TextView)findViewById(R.id.txtFRI2));
        txtMap.put("FRI3",(TextView)findViewById(R.id.txtFRI3));
        txtMap.put("FRI4",(TextView)findViewById(R.id.txtFRI4));

        //イベント登録
        for( String key : txtMap.keySet()){
            tempTextView = txtMap.get(key);

            tempTextView.setOnClickListener(new View.OnClickListener(){
                //ダイアログ内に渡すためのフィールド
                final TextView parentView = tempTextView;
                //テキストをクリックしたときのイベント
                @Override
                public void onClick(View View) {
                    dialog = new ListAlert(parent,View);

                    //ダイアログ内のテキストをクリックしたときのイベント
                    dialog.setTextViewOnClick(new View.OnClickListener(){
                        @Override
                        public void onClick( View view ){

                            Log.d("XXXXXX",(String)parentView.getText());
                            parentView.setText( (String)((TextView)view).getText() );

                            Drawable drawable = ResourcesCompat.getDrawable( getResources(),R.drawable.frame_style_yellow,null);

                            parentView.setBackground(drawable);

                            dialog.dismiss();


                        }
                    });

                    ArrayList<String> list = sqlAdapter.getArrayList("select distinct k_name from subject");
                    dialog.setList( list );
                    dialog.show();
                }
            });
        }

        //Sqliteから時間割を取得する
        sqlAdapter = new SQLiteAdapter(getApplicationContext(), new Database("mydb.db"));
        TableData table = sqlAdapter.getTableData("select * from subject where r_name='@r_name'".replace("@r_name",strRoom));
        for(TableRowData row : table ){
            String key = row.getValue(1);

            key = key + row.getValue(2);
            TextView tv = txtMap.get(key);
            tv.setText( row.getValue(3));

        }

        //リソースから作る場合、ファクトリーメソッドになる
        ArrayAdapter<CharSequence> aryAd = ArrayAdapter.createFromResource(
                this,
                R.array.k_name,
                R.layout.spinner_item_text8sp
        );

        aryAd.setDropDownViewResource(R.layout.spinner_dropdown_item);

    }

    //イベント
    public void btnUpdateOnClick( View view ){
        for( String key : txtMap.keySet() ){

            TextView tempTextView = ((TextView)txtMap.get(key));
            Log.d("key,value",key + "," + tempTextView.getText() );



        }
    }
}
