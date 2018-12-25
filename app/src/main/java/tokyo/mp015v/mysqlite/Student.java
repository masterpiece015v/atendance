package tokyo.mp015v.mysqlite;

public class Student extends Table{
    public Student( ){
        super( "student" );
    }

    @Override
    public void setFieldList() {
        this.addField(new TableField("st_no",FieldType.CHAR,5,true));
        this.addField(new TableField("st_name",FieldType.CHAR,20,false));
        this.addField(new TableField("ro_no_id",FieldType.CHAR,10,false));
    }

}
