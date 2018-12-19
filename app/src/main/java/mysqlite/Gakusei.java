package mysqlite;

public class Gakusei extends Table{
    public Gakusei( ){
        super( "gakusei" );
    }

    @Override
    public void setFieldList() {
        this.addField(new TableField("g_no",FieldType.CHAR,5,true));
        this.addField(new TableField("g_name",FieldType.CHAR,20,false));
        this.addField(new TableField("r_name",FieldType.CHAR,10,false));
    }

}
