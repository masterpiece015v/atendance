package tokyo.mp015v.mysqlite;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TableData implements Iterable<TableRowData>{
    Map<Integer,TableRowData> rows = new TreeMap<>();

    public void put( TableRowData rowdata ){
        Set<Integer> keys = rows.keySet();
        rows.put(keys.size(), rowdata );
    }

    public TableRowData get( Integer key ){
        return rows.get( key );
    }

    public int rowCount(){
        return rows.keySet().size();
    }

    @NonNull
    @Override
    public Iterator<TableRowData> iterator() {

        Iterator<TableRowData> iter = new Iterator<TableRowData>(){

            Iterator<Integer> iter2 = rows.keySet().iterator();

            @Override
            public boolean hasNext() {
                return iter2.hasNext();
            }

            @Override
            public TableRowData next() {
                return rows.get(iter2.next() );
            }
        };
        return iter;
    }
}
