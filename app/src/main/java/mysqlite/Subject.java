package mysqlite;

public class Subject extends Table {

    public Subject() {
        super("subject");
    }

    @Override
    public void setFieldList() {

        this.addField(new TableField("r_name",FieldType.CHAR,10,true));
        this.addField(new TableField("weekday",FieldType.CHAR,10,true));
        this.addField(new TableField("lessonTime",FieldType.INTEGER,0,true));
        this.addField(new TableField("k_name",FieldType.CHAR,10,false));
    }
}
