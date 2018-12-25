package tokyo.mp015v.atendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.watabe.atendance.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tokyo.mp015v.mysqlite.Database;
import tokyo.mp015v.mysqlite.Gakusei;
import tokyo.mp015v.mysqlite.SQLiteAdapter;
import tokyo.mp015v.mysqlite.Subject;
import tokyo.mp015v.mysqlite.TimeTable;

public class MainActivity extends AppCompatActivity {
    private Spinner spnRoom;
    private Spinner spnDate;
    private Spinner spnTime;
    private Spinner spnSub;
    private SQLiteAdapter sqlAdapter;
    public SQLiteAdapter getSqlAdapter(){return sqlAdapter;}
    private static int count = 0;
    private static String strRoom = null;
    private static String strDate = null;
    private static String strTime = null;
    private static String strSub = null;
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

        //データベースの作成（初回のみ）
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

            database.setValues("subject", new String[]{"1-1", Weekday.MON.toString(), "1", "商業簿記"});
            database.setValues("subject", new String[]{"1-1", Weekday.MON.toString(), "2", "商業簿記"});
            database.setValues("subject", new String[]{"1-1", Weekday.MON.toString(), "3", "Java"});
            database.setValues("subject", new String[]{"1-1", Weekday.TUE.toString(), "1", "Word"});
            database.setValues("subject", new String[]{"1-1", Weekday.TUE.toString(), "2", "情報概論"});
            database.setValues("subject", new String[]{"1-1", Weekday.WED.toString(), "1", "Excel"});
            database.setValues("subject", new String[]{"1-1", Weekday.WED.toString(), "2", "情報概論"});
            database.setValues("subject", new String[]{"1-1", Weekday.WED.toString(), "3", "工業簿記"});
            database.setValues("subject", new String[]{"1-1", Weekday.THU.toString(), "1", "工業簿記"});
            database.setValues("subject", new String[]{"1-1", Weekday.THU.toString(), "2", "職業指導"});
            database.setValues("subject", new String[]{"1-1", Weekday.THU.toString(), "3", "商業簿記"});
            database.setValues("subject", new String[]{"1-1", Weekday.FRI.toString(), "1", "Excel"});
            database.setValues("subject", new String[]{"1-1", Weekday.FRI.toString(), "2", "情報概論"});
            database.setValues("subject", new String[]{"1-1", Weekday.FRI.toString(), "3", "工業簿記"});

            database.setValues("subject", new String[]{"1-2", Weekday.MON.toString(), "1", "ビジネス実務"});
            database.setValues("subject", new String[]{"1-2", Weekday.MON.toString(), "2", "PowerPoint"});
            database.setValues("subject", new String[]{"1-2", Weekday.MON.toString(), "3", "Access"});
            database.setValues("subject", new String[]{"1-2", Weekday.TUE.toString(), "1", "FP"});
            database.setValues("subject", new String[]{"1-2", Weekday.TUE.toString(), "2", "FP"});
            database.setValues("subject", new String[]{"1-2", Weekday.WED.toString(), "1", "プレゼン"});
            database.setValues("subject", new String[]{"1-2", Weekday.WED.toString(), "2", "消費税"});
            database.setValues("subject", new String[]{"1-2", Weekday.WED.toString(), "3", "所得税"});
            database.setValues("subject", new String[]{"1-2", Weekday.THU.toString(), "1", "法人税"});
            database.setValues("subject", new String[]{"1-2", Weekday.THU.toString(), "2", "Webページ"});
            database.setValues("subject", new String[]{"1-2", Weekday.THU.toString(), "3", "ビジネス会計"});
            database.setValues("subject", new String[]{"1-2", Weekday.FRI.toString(), "1", "ビジネス会計"});
            database.setValues("subject", new String[]{"1-2", Weekday.FRI.toString(), "2", "PowerPoint"});
            database.setValues("subject", new String[]{"1-2", Weekday.FRI.toString(), "3", "Access"});
            this.sqlAdapter = new SQLiteAdapter(this.getApplicationContext(), database);
            count = count + 1;
        }

        if( count > 0 ){
            this.sqlAdapter = new SQLiteAdapter(this.getApplicationContext(), new Database( "mydb.db"));
        }

        //クラスのスピナーに登録
        String[] aryRoom = this.sqlAdapter.getSpinnerString("select distinct r_name from gakusei");
        ArrayAdapter<String> aryAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                aryRoom
        );
        aryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.spnRoom.setAdapter(aryAdapter);
        //以前選択をしていればそれを表示
        int num = 0;
        if( strRoom != null ){
            for( int i = 0 ; i < aryRoom.length ; i++ ){
                if( strRoom.equals(aryRoom[i]) ){
                    num = i;
                    break;
                }
            }
        }
        this.spnRoom.setSelection(num);

        //日付のスピナーに登録
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YY年MM月dd日 E");
        String[] aryDate = new String[7];
        for (int i = 0; i < 7; i++) {
            aryDate[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        ArrayAdapter<String> dayAryAd = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                aryDate
        );
        dayAryAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spnDate.setAdapter(dayAryAd);
        num = 0;
        if( strDate != null ){
            for( int i = 0 ; i < aryDate.length ; i++ ){
                if( strDate.equals(aryDate[i]) ){
                    num = i;
                    break;
                }
            }
        }
        this.spnDate.setSelection( num );

        //時間のスピナーに登録
        String[] aryTime = new String[]{"1","2","3","4"};
        ArrayAdapter<String> timeAryAd = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                aryTime
                );
        timeAryAd.setDropDownViewResource( R.layout.spinner_dropdown_item);
        spnTime.setAdapter(timeAryAd);
        num = 0;
        if( strTime != null ){
            for( int i = 0 ; i < aryTime.length ; i++ ){
                if( strTime.equals(aryTime[i]) ){
                    num = i;
                    break;
                }
            }
        }
        this.spnTime.setSelection( num );
    }

    //イベントクラス
    //spnRoomのイベント
    class SpnRoomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            changeSubject();
            strRoom = (String)((Spinner)(adapterView)).getSelectedItem();
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
            strDate = (String)((Spinner)(adapterView)).getSelectedItem();
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
            strTime = (String)((Spinner)(adapterView)).getSelectedItem();
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
                    sql2 = sql2.replace( "@weekday" , Weekday.SUN.toString() );
                    break;
                case Calendar.MONDAY:
                    sql2 = sql2.replace( "@weekday" , Weekday.MON.toString() );
                    break;
                case Calendar.TUESDAY:
                    sql2 = sql2.replace( "@weekday" , Weekday.TUE.toString() );
                    break;
                case Calendar.WEDNESDAY:
                    sql2 = sql2.replace( "@weekday" , Weekday.WED.toString() );
                    break;
                case Calendar.THURSDAY:
                    sql2 = sql2.replace( "@weekday" , Weekday.TUE.toString() );
                    break;
                case Calendar.FRIDAY:
                    sql2 = sql2.replace( "@weekday" , Weekday.FRI.toString() );
                    break;
                case Calendar.SATURDAY:
                    sql2 = sql2.replace( "@weekday" , Weekday.SAT.toString() );
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
                            R.layout.spinner_item,
                            k_list
                    );
                    aryAd.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
                        R.layout.spinner_item,
                        new String[]{}
                );
                aryAd.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spnSub.setAdapter( aryAd );
            }
        }catch(ParseException e){
            e.printStackTrace();
        }

    }

    //出欠登録を開く
    public void btnNextPageOnClick( View view ){
        if( spnSub.getSelectedItem() != null ) {
            //インテントの作成
            Intent intent = new Intent(this, SubActivity.class);
            intent.putExtra("txtRoom", (String) spnRoom.getSelectedItem());
            intent.putExtra("txtDate", (String) spnDate.getSelectedItem());
            intent.putExtra("txtLessonTime", (String) spnTime.getSelectedItem());
            intent.putExtra("txtSubject", (String) spnSub.getSelectedItem());

            startActivity(intent);
        }else{
            MyAlert alert = new MyAlert(this,"科目を選択してください","OK");
            alert.show();
        }
    }
    //時間割作成を開く
    public void btnMakeTimeTableOnClick( View view ){
        Intent intent = new Intent( this,MakeTimeTableActivity.class );
        intent.putExtra("txtRoom",(String)spnRoom.getSelectedItem());
        startActivity(intent);
    }
}
