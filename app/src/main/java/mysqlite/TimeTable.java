package mysqlite;

public class TimeTable extends Table {
    public TimeTable(){
        super("timetable");
    }
    @Override
    public void setFieldList() {
        this.addField(new TableField("g_no",FieldType.CHAR,5,true));
        this.addField(new TableField("date",FieldType.CHAR,20,true));
        this.addField(new TableField("lessonTime",FieldType.INTEGER,0,true));
        this.addField(new TableField("k_name",FieldType.CHAR,10,false));
        this.addField(new TableField("record",FieldType.INTEGER,0,false));
    }
}
