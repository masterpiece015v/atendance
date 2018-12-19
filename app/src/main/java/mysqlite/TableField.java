package mysqlite;

public class TableField {
    private String name;
    private FieldType type;
    private Integer size;
    private Boolean primarykey;

    public TableField( String name , FieldType type , Integer size , Boolean primarykey ){
        this.name = name;
        this.type = type;
        this.size = size;
        this.primarykey = primarykey;
    }

    public String getName(){return name;}
    public String getCreateTable(){
        String sql = null;
        if(type==FieldType.CHAR) {
            sql = name + " " + type.toString() + "(" + size.toString() + ")";
        }else{
            sql = name + " " + type.toString();
        }
        return sql;
    }
    public FieldType getFieldType(){return type;}
    public Boolean equals( String name ){
        return this.name.equals(name);

    }
    public Boolean getPrimaryKey(){
        return this.primarykey;
    }

}