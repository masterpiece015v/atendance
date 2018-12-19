package com.example.watabe.atendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.websarva.wings.android.atendanceapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

import mysqlite.Database;
import mysqlite.Gakusei;
import mysqlite.SQLiteAdapter;
import mysqlite.Subject;
import mysqlite.TimeTable;

public class MainActivity extends AppCompatActivity {
    private Spinner spnRoom;
    private Spinner spnDate;
    private Spinner spnTime;
    private Spinner spnSub;
    private SQLiteAdapter sqlAdapter;
    public SQLiteAdapter getSqlAdapter(){return sqlAdapter;}
    private static int count = 0;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d( "count",String.valueOf(count));

        //Viewとの関連付け
        spnRoom = findViewById(R.id.spnRoom);
        spnDate = findViewById(R.id.spnDate);
        spnTime = findViewById(R.id.spnTime);
        spnSub = findViewById(R.id.spnSub);

        //イベントの登録
        spnRoom.setOnItemSelectedListener(new SpnRoomOnItemSelectedListener());
        spnDate.setOnItemSelectedListener(new SpnDateOnItemSelectedListener());
        spnTime.setOnItemSelectedListener(new SpnTimeOnItemSelectedListener());

        //データベースの作成
        if( count == 0) {
            //mydb.dbの削除
            try {
                getApplicationContext().deleteDatabase("mydb.db");
            } catch (Exception e) {
                e.printStackTrace();//mydb.dbがなかった時
            }

            database = new Database("mydb.db");

            database.addTable(new Gakusei());
            database.addTable(new Subject());
            database.addTable(new TimeTable());

            //データの登録
            database.setValues("gakusei", new String[]{"10001", "川下", "1-1"});
            database.setValues("gakusei", new String[]{"10003", "坂口", "1-1"});
            database.setValues("gakusei", new String[]{"10005", "谷間", "1-1"});
            database.setValues("gakusei", new String[]{"10008", "堂山", "1-1"});
            database.setValues("gakusei", new String[]{"10009", "二井内", "1-1"});

            database.setValues("subject", new String[]{"1-1", DayOfWeek.MONDAY.toString(), "1", "商業簿記"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.MONDAY.toString(), "2", "商業簿記"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.MONDAY.toString(), "3", "Java"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.TUESDAY.toString(), "1", "Word"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.TUESDAY.toString(), "2", "情報概論"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.WEDNESDAY.toString(), "1", "Excel"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.WEDNESDAY.toString(), "2", "情報概論"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.WEDNESDAY.toString(), "3", "工業簿記"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.THURSDAY.toString(), "1", "工業簿記"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.THURSDAY.toString(), "2", "職業指導"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.THURSDAY.toString(), "3", "商業簿記"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.FRIDAY.toString(), "1", "Excel"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.FRIDAY.toString(), "2", "情報概論"});
            database.setValues("subject", new String[]{"1-1", DayOfWeek.FRIDAY.toString(), "3", "工業簿記"});

            database.setValues("subject", new String[]{"1-2", DayOfWeek.MONDAY.toString(), "1", "ビジネス実務"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.MONDAY.toString(), "2", "PowerPoint"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.MONDAY.toString(), "3", "Access"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.TUESDAY.toString(), "1", "FP"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.TUESDAY.toString(), "2", "FP"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.WEDNESDAY.toString(), "1", "プレゼン"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.WEDNESDAY.toString(), "2", "消費税"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.WEDNESDAY.toString(), "3", "所得税"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.THURSDAY.toString(), "1", "法人税"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.THURSDAY.toString(), "2", "Webページ"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.THURSDAY.toString(), "3", "ビジネス会計"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.FRIDAY.toString(), "1", "ビジネス会計"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.FRIDAY.toString(), "2", "PowerPoint"});
            database.setValues("subject", new String[]{"1-2", DayOfWeek.FRIDAY.toString(), "3", "Access"});
            this.sqlAdapter = new SQLiteAdapter(this.getApplicationContext(), database);
            count = count + 1;
        }

        if( count > 0 ){
            this.sqlAdapter = new SQLiteAdapter(this.getApplicationContext(), new Database( "mydb.db"));
        }

        //クラスのスピナーに登録
        ArrayAdapter<String> aryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                this.sqlAdapter.getSpinnerString("select distinct r_name from gakusei")
        );
        aryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spnRoom.setAdapter(aryAdapter);

        //日付のスピナーに登録
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YY年MM月dd日 E");
        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        ArrayAdapter<String> dayAryAd = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                days
        );
        dayAryAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(dayAryAd);

        //時間のスピナーに登録
        ArrayAdapter<String> timeAryAd = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"1", "2", "3", "4"}
                );
        spnTime.setAdapter(timeAryAd);
    }

    //イベントクラス
    //spnRoomのイベント
    class SpnRoomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            changeSubject();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    //spnDateのイベント
    class SpnDateOnItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            changeSubject();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    //spnTimeのイベント
    class SpnTimeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            changeSubject();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    //科目名をチェンジする
    public void changeSubject(){
        String room = (String)spnRoom.getSelectedItem();
        String date = (String)spnDate.getSelectedItem();
        String lessonTime = (String)spnTime.getSelectedItem();

        SimpleDateFormat format = new SimpleDateFormat("YY年MM月dd日 E");

        try {
            Date d = format.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime( d );

            Log.d("date",d.toString() );

            //クラスが受講している全ての科目
            String sql = "select distinct k_name from subject where r_name='@r_name'";
            sql = sql.replace("@r_name",room);
            //該当するクラス
            String sql2 = "select distinct k_name from subject where r_name='@r_name' and weekday='@weekday' and lessonTime=@lessonTime";
            sql2 = sql2.replace("@r_name",room);

            switch( c.get(Calendar.DAY_OF_WEEK)){
                case Calendar.SUNDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.SUNDAY.toString() );
                    break;
                case Calendar.MONDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.MONDAY.toString() );
                    break;
                case Calendar.TUESDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.TUESDAY.toString() );
                    break;
                case Calendar.WEDNESDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.WEDNESDAY.toString() );
                    break;
                case Calendar.THURSDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.TUESDAY.toString() );
                    break;
                case Calendar.FRIDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.FRIDAY.toString() );
                    break;
                case Calendar.SATURDAY:
                    sql2 = sql2.replace( "@weekday" , DayOfWeek.SATURDAY.toString() );
                    break;
            }
            sql2 = sql2.replace("@lessonTime", lessonTime);

            Log.d("weekday", String.valueOf( c.get(Calendar.DAY_OF_WEEK) ));
            Log.d("sql2",sql2);

            String kamoku = null;
            String[] k_list = null;

            try {
                //sqlAdapterがエラーになるので、
                kamoku = sqlAdapter.getSpinnerString(sql2)[0];
                k_list = sqlAdapter.getSpinnerString(sql);
                if(sqlAdapter!=null){
                    ArrayAdapter<String> aryAd = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            k_list
                    );
                    spnSub.setAdapter( aryAd );
                }
                //その時間の科目を表示する
                for(int j= 0 ; j < k_list.length ; j++ ){
                    if(k_list[j].equals(kamoku)){
                        spnSub.setSelection( j );
                    }
                }

            }catch(Exception e ){
                //科目が存在しない時は科目を空白にする
                ArrayAdapter<String> aryAd = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        new String[]{}
                );
                spnSub.setAdapter( aryAd );
            }
        }catch(ParseException e){
            e.printStackTrace();
        }

    }

    //buttonOnClick
    public void btnNextPageOnClick( View view ){
        //インテントの作成
        Intent intent = new Intent(this,SubActivity.class);
        intent.putExtra("txtRoom",(String)spnRoom.getSelectedItem());
        intent.putExtra("txtDate",(String)spnDate.getSelectedItem());
        intent.putExtra("txtLessonTime",(String)spnTime.getSelectedItem());
        intent.putExtra("txtSubject",(String)spnSub.getSelectedItem());

        startActivity( intent );

    }
}
