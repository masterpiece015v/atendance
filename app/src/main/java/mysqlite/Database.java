package mysqlite;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private String name;
    private Map<String,Table> tableMap = new HashMap<>();

    public Database( String name ){ this.name = name; }
    
    //テーブルにデータを挿入する
    public void setValues( String tableName , String[] values ){
        Table table = tableMap.get(tableName);
        table.setValue( values );
    }
    //テーブルの追加
    public void addTable( Table table){
        Log.d("addTable",table.getName() );
        this.tableMap.put( table.getName() , table );
    }
    //データベース名の取得
    public String getName(){return this.name;}

    public String getCreateTable( String tableName ){
        return tableMap.get(tableName).getCreateTable();
    }
    public Table getTable( String name ){
        return tableMap.get(name);
    }
    public Map<String,Table> getTableMap(){return tableMap;}
}
