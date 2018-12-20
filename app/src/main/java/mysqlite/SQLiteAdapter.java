package mysqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLiteAdapter {

    SQLiteOpenHelper helper;
    Database data;
    public static final int DB_VER = 3;
    SQLiteDatabase sqliteDb;

    //コンストラクタ
    public SQLiteAdapter( Context context ,String dbName){

        helper = new SQLiteOpenHelper(context , dbName ,null , DB_VER) {
            @Override
            public void onCreate(SQLiteDatabase sqlLiteDb ) {
                Map<String,Table> map = data.getTableMap();
                for( String key : map.keySet() ){
                    Log.d("create",map.get(key).getCreateTable() );
                    //sqlLiteDb.execSQL( map.get(key).getDropTable() );
                    sqlLiteDb.execSQL( map.get(key).getCreateTable() );
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqlLiteDb, int i, int i1) {
                Map<String,Table> map = data.getTableMap();
                for( String key : map.keySet() ){
                    sqlLiteDb.execSQL( map.get(key).getDropTable() );
                }
                onCreate( sqlLiteDb );
            }
        };

        //書き込み用データベースの取得
        sqliteDb = helper.getWritableDatabase();

        Map<String,Table> map = data.getTableMap();

        for( String key : map.keySet() ){
            //重複が発生しないように一度データを消す
            try {
                sqliteDb.execSQL(map.get(key).getDeleteTable());
            }catch(SQLiteException e){
                e.printStackTrace();
            }
            //追加する
            List<String> insert = map.get(key).getInsertTable();
            for( String item : insert ){
                Log.d("insert",item );
                sqliteDb.execSQL( item );
            }
        }
    }

    //コンストラクタ
    public SQLiteAdapter( Context context ,Database database){
        data = database;

        helper = new SQLiteOpenHelper(context , database.getName() ,null , DB_VER) {
            @Override
            public void onCreate(SQLiteDatabase sqlLiteDb ) {
                Map<String,Table> map = data.getTableMap();
                for( String key : map.keySet() ){
                    Log.d("create",map.get(key).getCreateTable() );
                    //sqlLiteDb.execSQL( map.get(key).getDropTable() );
                    sqlLiteDb.execSQL( map.get(key).getCreateTable() );
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqlLiteDb, int i, int i1) {
                Map<String,Table> map = data.getTableMap();
                for( String key : map.keySet() ){
                    sqlLiteDb.execSQL( map.get(key).getDropTable() );
                }
                onCreate( sqlLiteDb );
            }
        };

        //書き込み用データベースの取得
        sqliteDb = helper.getWritableDatabase();

        Map<String,Table> map = data.getTableMap();

        for( String key : map.keySet() ){
            //重複が発生しないように一度データを消す
            try {
                sqliteDb.execSQL(map.get(key).getDeleteTable());
            }catch(SQLiteException e){
                e.printStackTrace();
            }
            //追加する
            List<String> insert = map.get(key).getInsertTable();
            for( String item : insert ){
                Log.d("insert",item );
                sqliteDb.execSQL( item );
            }
        }
    }

    public Cursor getCursor( String sql ){

        Cursor cursor = sqliteDb.rawQuery( sql ,null );

        return cursor;
    }

    //Spinnerに使用する文字列配列を返す
    public String[] getSpinnerString( String sql ){
        //カーソルの取り出し
        Cursor cursor = sqliteDb.rawQuery( sql ,null);
        cursor.moveToFirst();

        String[] items = new String[ cursor.getCount() ];

        for( int i = 0 ; i < cursor.getCount() ; i++ ){
            items[i] = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public ArrayList<String> getArrayList(String sql ){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = sqliteDb.rawQuery( sql , null );
        cursor.moveToFirst();

        for( int i = 0 ; i < cursor.getCount() ; i++ ){
            list.add( cursor.getString(0) );
            cursor.moveToNext();
        }
        return list;
    }

    //TableDataオブジェクトにして返す
    public TableData getTableData( String sql ){
        Cursor cursor = sqliteDb.rawQuery( sql ,null);
        cursor.moveToFirst();

        TableData table = new TableData();

        for( int i = 0 ; i < cursor.getCount() ; i++ ){
            TableRowData data = new TableRowData();
            for( int j = 0 ; j < cursor.getColumnCount() ; j++ ) {
                data.put( cursor.getString(j) );
                Log.d( "sqlAdapter",cursor.getString(j) );
            }
            cursor.moveToNext();
            table.put( data );
        }

        cursor.close();

        return table;
    }
    //列番号を指定してSpinnerに使用する文字列配列を返却する
    public String[] getSpinnerString( String sql ,int column){
        //カーソルの取り出し
        Cursor cursor = sqliteDb.rawQuery( sql ,null);
        cursor.moveToFirst();

        String[] items = new String[ cursor.getCount() ];

        for( int i = 0 ; i < cursor.getCount() ; i++ ){
            items[i] = cursor.getString(column);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }
    //sqlを実行する
    public void execSQL( String sql ){
        sqliteDb = helper.getWritableDatabase();

        try {
            sqliteDb.execSQL(sql);
        }catch(SQLiteConstraintException e){
            Log.d("error","UNIQUE" );
        }
    }


}


