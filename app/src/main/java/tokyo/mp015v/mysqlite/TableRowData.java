package tokyo.mp015v.mysqlite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TableRowData {
    Map<Integer,String> cols = new HashMap<>();

    public void put( String value ){
        Set<Integer> cols = this.cols.keySet();
        this.cols.put( cols.size() , value );
    }

    public String getValue( Integer columns ){
        return cols.get( columns );
    }
}
