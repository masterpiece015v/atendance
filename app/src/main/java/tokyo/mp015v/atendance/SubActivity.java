package tokyo.mp015v.atendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

//import com.websarva.wings.android.atendanceapp.R;

import tokyo.mp015v.mysqlite.Database;
import tokyo.mp015v.mysqlite.SQLiteAdapter;
import tokyo.mp015v.mysqlite.TableData;
import tokyo.mp015v.mysqlite.TableRowData;

public class SubActivity extends AppCompatActivity {
    private TextView txtRoom;
    private TextView txtDate;
    private TextView txtLessonTime;
    private TextView txtSubject;
    private ViewGroup tblGakusei;
    SQLiteAdapter sqlAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tokyo.mp015v.atendance.R.layout.activity_sub);

        //ビューを関連付ける
        txtRoom = findViewById(tokyo.mp015v.atendance.R.id.txtRoom);
        txtDate = findViewById(tokyo.mp015v.atendance.R.id.txtDate);
        txtLessonTime = findViewById(tokyo.mp015v.atendance.R.id.txtLessonTime);
        txtSubject = findViewById(tokyo.mp015v.atendance.R.id.txtSubject);

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
        sqlAdapter = new SQLiteAdapter(getApplicationContext(),new Database("mydb.db"));
        String sql = "select distinct g_no, g_name from gakusei where r_name='@r_name'";
        sql = sql.replace( "@r_name",strRoom );

        tblGakusei = (ViewGroup)findViewById(tokyo.mp015v.atendance.R.id.tblGakusei);
        TableData tableData = sqlAdapter.getTableData( sql );
        int i = 0;

        for( TableRowData row : tableData){
            Log.d("tableRowdata",row.getValue(0) );
            getLayoutInflater().inflate(tokyo.mp015v.atendance.R.layout.table_row,tblGakusei);
            TableRow tr = (TableRow)tblGakusei.getChildAt(i);
            ((TextView)(tr.getChildAt(0))).setText(row.getValue(0));
            ((TextView)(tr.getChildAt(1))).setText(row.getValue(1));
            i++;
        }

        //すでに出席情報が存在するかをチェックする
        String sql2 = "select * from timetable where g_no in ( select g_no from gakusei where r_name='@r_name' ) and date='@date' and k_name='@k_name'";
        sql2 = sql2.replace("@r_name",strRoom);
        sql2 = sql2.replace( "@date",strDate);
        sql2 = sql2.replace("@k_name",strSubject);

        TableData t2 = sqlAdapter.getTableData( sql2 );
        for( TableRowData row : t2 ){
            String g_no = row.getValue(0);
            String record = row.getValue(4);
            for( int j = 0 ; j < tblGakusei.getChildCount(); j++ ){
                TableRow tr = (TableRow)tblGakusei.getChildAt(j);
                TextView temp = ((TextView)(tr.getChildAt(0)));
                if( temp.getText().equals(g_no)){
                    Log.d("equals",g_no + "," + temp.getText() );
                    RadioGroup rg = ((RadioGroup)(tr.getChildAt(2)));
                    ((RadioButton)rg.getChildAt(Integer.parseInt(record))).setChecked( true );
                }
            }
            Log.d("g_no,record", g_no + "," + record );
        }
    }

    //イベント
    //出席を追加するボタン
    public void btnRegisterOnClick( View view ){
        for( int i = 0 ; i < tblGakusei.getChildCount() ; i++ ){
            TableRow row = (TableRow)tblGakusei.getChildAt(i);
            RadioGroup rg = ((RadioGroup)(row.getChildAt(2)));
            RadioButton rb = findViewById( rg.getCheckedRadioButtonId() );
            TextView tv = ((TextView)(row.getChildAt(0)));
            //Log.d( String.valueOf( tv.getText() ) , String.valueOf( rb.getText() ));

            String g_no = String.valueOf( tv.getText() );
            String date = String.valueOf(txtDate.getText() );
            String lessonTime = String.valueOf(txtLessonTime.getText() );
            String k_name = String.valueOf(txtSubject.getText() );

            String record = null;
            if( String.valueOf(rb.getText()).equals("出席")){
                record = "0";
            }else if( String.valueOf(rb.getText()).equals("欠席")){
                record = "2";
            }else{
                record = "1";
            }

            String insert = "insert into timetable (g_no,date,lessonTime,k_name,record) values('@v1','@v2',@v3,'@v4',@v5)";
            insert = insert.replace("@v1",g_no);
            insert = insert.replace( "@v2",date);
            insert = insert.replace( "@v3",lessonTime);
            insert = insert.replace( "@v4",k_name );
            insert = insert.replace( "@v5",record );
            Log.d( "insert" , insert );
            sqlAdapter.execSQL( insert );
        }
        MyAlert alert = new MyAlert(this,"追加されました","OK");
        alert.show();

    }


    public void btnUpdateOnClick( View view ) {
        for (int i = 0; i < tblGakusei.getChildCount(); i++) {
            TableRow row = (TableRow) tblGakusei.getChildAt(i);
            RadioGroup rg = ((RadioGroup) (row.getChildAt(2)));
            RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
            TextView tv = ((TextView) (row.getChildAt(0)));
            //Log.d( String.valueOf( tv.getText() ) , String.valueOf( rb.getText() ));

            String g_no = String.valueOf(tv.getText());
            String date = String.valueOf(txtDate.getText());
            String lessonTime = String.valueOf(txtLessonTime.getText());
            String k_name = String.valueOf(txtSubject.getText());

            String record = null;
            if (String.valueOf(rb.getText()).equals("出席")) {
                record = "0";
            } else if (String.valueOf(rb.getText()).equals("欠席")) {
                record = "2";
            } else {
                record = "1";
            }

            String update = "update timetable set record='@record',k_name='@k_name' where g_no='@g_no' and date='@date' and lessonTime='@lessonTime'";
            update = update.replace("@g_no", g_no);
            update = update.replace("@date", date);
            update = update.replace("@lessonTime", lessonTime);
            update = update.replace("@record", record);
            update = update.replace("@k_name",k_name);
            Log.d("update", update);
            sqlAdapter.execSQL(update);
        }

        MyAlert alert = new MyAlert(this,"更新されました","OK");
        alert.show();
    }
}
