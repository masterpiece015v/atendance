package tokyo.mp015v.atendance;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tokyo.mp015v.mysqlite.Database;
import tokyo.mp015v.mysqlite.SQLiteAdapter;
import tokyo.mp015v.mysqlite.TableData;
import tokyo.mp015v.mysqlite.TableRowData;

public class MakeTimeTableActivity extends AppCompatActivity {
    //フィールド
    private Map<String,TextView> txtMap = new HashMap<>();
    private SQLiteAdapter sqlAdapter;
    private MakeTimeTableActivity parent;
    private String strRoom;
    private ListAlert dialog;
    private TextView tempTextView;

    //メソッド
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tokyo.mp015v.atendance.R.layout.activity_make_time_table);

        //
        parent = this;
        //インテント
        Intent intent = getIntent();
        this.strRoom = intent.getStringExtra("txtRoom");
        //クラス名の挿入
        ((TextView)findViewById(tokyo.mp015v.atendance.R.id.txtRoom)).setText( strRoom );
        //MapにtxtXXXYを登録する
        txtMap.put("MON1",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtMON1));
        txtMap.put("MON2",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtMON2));
        txtMap.put("MON3",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtMON3));
        txtMap.put("MON4",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtMON4));

        txtMap.put("TUE1",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTUE1));
        txtMap.put("TUE2",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTUE2));
        txtMap.put("TUE3",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTUE3));
        txtMap.put("TUE4",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTUE4));

        txtMap.put("WED1",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtWED1));
        txtMap.put("WED2",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtWED2));
        txtMap.put("WED3",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtWED3));
        txtMap.put("WED4",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtWED4));

        txtMap.put("THU1",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTHU1));
        txtMap.put("THU2",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTHU2));
        txtMap.put("THU3",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTHU3));
        txtMap.put("THU4",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtTHU4));

        txtMap.put("FRI1",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtFRI1));
        txtMap.put("FRI2",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtFRI2));
        txtMap.put("FRI3",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtFRI3));
        txtMap.put("FRI4",(TextView)findViewById(tokyo.mp015v.atendance.R.id.txtFRI4));

        //イベント登録
        for( String key : txtMap.keySet()){
            tempTextView = txtMap.get(key);

            //科目名をクリックしたらリストを出すためのイベント登録
            tempTextView.setOnClickListener(new View.OnClickListener(){
                //ダイアログ内に渡すためのフィールド
                final TextView parentView = tempTextView;
                //テキストをクリックしたときのイベント
                @Override
                public void onClick(View View) {
                    dialog = new ListAlert(parent);

                    //ダイアログ内のテキストをクリックしたときのイベント
                    dialog.setTextViewOnClick(new View.OnClickListener(){
                        @Override
                        public void onClick( View view ){

                            Log.d("XXXXXX",(String)parentView.getText());

                            String setText = (String)((TextView)view).getText();

                            parentView.setText( setText );

                            if( setText.length() >= 6 ){
                                parentView.setTextSize( 8 );
                            }else{
                                parentView.setTextSize( 12 );
                            }

                            Drawable drawable = ResourcesCompat.getDrawable( getResources(), tokyo.mp015v.atendance.R.drawable.frame_style_yellow,null);
                            parentView.setBackground(drawable);

                            dialog.dismiss();

                        }
                    });

                    //ダイアログのリストに表示する内容の登録
                    ArrayList<String> list = sqlAdapter.getArrayList("select distinct k_name from subject");
                    list.add(0,"");
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

            String setText = row.getValue( 3 );
            tv.setText( setText );

            //テキストのサイズを変更する
            if( setText.length() >= 6 ){
                tv.setTextSize( 8 );
            }else{
                tv.setTextSize( 12 );
            }
        }

        //リソースから作る場合、ファクトリーメソッドになる
        ArrayAdapter<CharSequence> aryAd = ArrayAdapter.createFromResource(
                this,
                tokyo.mp015v.atendance.R.array.k_name,
                tokyo.mp015v.atendance.R.layout.spinner_item_text8sp
        );

        aryAd.setDropDownViewResource(tokyo.mp015v.atendance.R.layout.spinner_dropdown_item);

    }

    //更新ボタンのイベント
    public void btnUpdateOnClick( View view ){
        for( String key : txtMap.keySet() ){

            TextView tempTextView = ((TextView)txtMap.get(key));
            String k_name = (String)tempTextView.getText();
            Log.d("key,value",key + "," + tempTextView.getText() );
            Matcher m = Pattern.compile("[\\s\\S]{1,3}").matcher(key);
            m.find();
            String weekday = m.group();
            m.find();
            String lessonTime = m.group();
            Log.d("btnUpDateOnClick", weekday + "," + lessonTime);

            String select = "select * from subject where r_name='@r_name' and weekday='@weekday' and lessonTime=@lessonTime";
            select =  select.replace("@r_name",this.strRoom);
            select = select.replace("@weekday",weekday);
            select = select.replace("@lessonTime",lessonTime);
            TableData table = sqlAdapter.getTableData(select );

            if( table.rowCount() == 1 && k_name.length() > 0 ) {
                String sql = "update subject set k_name='@k_name' where r_name='@r_name' and weekday='@weekday' and lessonTime=@lessonTime";
                sql = sql.replace("@k_name", k_name);
                sql = sql.replace("@r_name", this.strRoom);
                sql = sql.replace("@weekday", weekday);
                sql = sql.replace("@lessonTime", lessonTime);
                sqlAdapter.execSQL(sql);
            }else if( table.rowCount() == 1 && k_name.length() == 0 ){
                String sql = "delete from subject where r_name='@r_name' and weekday='@weekday' and lessonTime=@lessonTime";
                sql = sql.replace("@r_name", this.strRoom);
                sql = sql.replace("@weekday", weekday);
                sql = sql.replace("@lessonTime", lessonTime);
                sqlAdapter.execSQL(sql);
            }else if( table.rowCount() == 0 && k_name.length() > 0 ){
                String sql = "insert into subject( r_name , weekday , lessonTime , k_name ) values ('@r_name','@weekday', @lessonTime, '@k_name')";
                sql = sql.replace( "@r_name",this.strRoom);
                sql = sql.replace( "@weekday",weekday);
                sql = sql.replace("@lessonTime",lessonTime);
                sql = sql.replace("@k_name",k_name);
                sqlAdapter.execSQL( sql );
            }

        }
        MyAlert alert = new MyAlert(this,"更新できました","OK");
        alert.show();

    }
}
