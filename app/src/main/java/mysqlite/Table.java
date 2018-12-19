package mysqlite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public abstract class Table {
    protected String name;
    protected List<TableField> fieldList = new ArrayList<>();
    protected List<String[]> fieldValues = new ArrayList<>();

    public Table( String name ){
        this.name = name;
        setFieldList();
    }

    public void addField( TableField field ){
        fieldList.add( field );
    }
    public String getName(){return this.name;}
    public String getCreateTable(){
        String sql = "create table " + this.name + " (";

        String p_sql = "";
        int p_count = 0;
        //項目を作る
        for( int i = 0 ; i < fieldList.size() ; i++ ){
            if( i == 0 ){
                sql = sql + fieldList.get(i).getCreateTable();
            }else{
                sql = sql + "," + fieldList.get(i).getCreateTable();
            }

            if( fieldList.get(i).getPrimaryKey() ){
                if( p_count == 0 ){
                    p_sql = "primary key (" + fieldList.get(i).getName().toString() ;
                }else{
                    p_sql = p_sql + "," + fieldList.get(i).getName() ;
                }
                p_count = p_count + 1;
            }

        }

        if( p_count > 0 ){
            sql = sql + "," + p_sql + "));";
        }else{
            sql = sql + "));";
        }

        return sql;

    }
    public String getDeleteTable(){
        return "delete from " + this.name + ";";
    }
    public String getDropTable(){
        return "drop table " + name + ";";
    }
    public List<String> getInsertTable(){
        List<String> list = new ArrayList<>();
        //バリューがなくなるまで
        for( String[] value : fieldValues ){
            String sql = "insert into @tbl (@fields) values (@values);";

            sql = sql.replace( "@tbl", name );

            String fields = "";
            String values = "";

            for( int i = 0 ; i < fieldList.size() ; i++ ){
                if( i == 0 ){
                    fields = fieldList.get(i).getName();
                    if( fieldList.get(i).getFieldType()==FieldType.INTEGER){
                        values = value[i];
                    }else{
                        values = "'" + value[i] + "'";
                    }
                }else{
                    fields = fields + "," + fieldList.get(i).getName();
                    if( fieldList.get(i).getFieldType()==FieldType.INTEGER ){
                        values = values + "," + value[i];
                    }else{
                        values = values + "," + "'" + value[i] + "'";
                    }
                }
            }
            sql = sql.replace("@fields",fields);
            sql = sql.replace("@values",values);
            list.add( sql );
        }

        return list;
    }
    public void setValue(String[] values){
        
        if( values.length == fieldList.size() ){
            fieldValues.add( values );
        }else{
            System.out.println("文字数が合いません");
        }
        
    }

    public Boolean equals( String tableName ){
        return name.equals( tableName );

    }

    abstract public void setFieldList();
}