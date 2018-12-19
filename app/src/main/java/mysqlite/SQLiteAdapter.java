package mysqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SQLiteAdapter {

    SQLiteOpenHelper helper;
    Database data;
    public static final int DB_VER = 3;
    SQLiteDatabase sqliteDb;

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

    public void execSQL( String sql ){
        sqliteDb = helper.getWritableDatabase();
        sqliteDb.execSQL( sql );

    }


}


