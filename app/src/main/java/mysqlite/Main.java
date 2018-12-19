/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlite;

import java.time.DayOfWeek;

/**
 *
 * @author watabe
 */
public class Main {
    public static void main(String[] args){

        //データベースの作成
        Database database = new Database("mydb.db");

        database.addTable( new Gakusei());
        database.setValues("gakusei", new String[]{"10001","川下","1-1"});
        database.setValues("gakusei", new String[]{"10003","坂口","1-1"});
        database.setValues("gakusei", new String[]{"10005","谷間","1-2"});
        database.setValues("gakusei", new String[]{"10008","堂山","1-2"});
        database.setValues("gakusei", new String[]{"10009","二井内","1-3"});

        database.addTable( new Subject());
        database.setValues("subject",new String[]{"1-1",DayOfWeek.MONDAY.toString(),"1","簿記"});
        database.setValues("subject",new String[]{"1-1",DayOfWeek.MONDAY.toString(),"2","簿記"});
        database.setValues("subject",new String[]{"1-1",DayOfWeek.MONDAY.toString(),"3","Java"});
        //System.out.println( database.getCreateTable("gakusei"));
        
        for( String item : database.getTable("gakusei").getInsertTable()){
            System.out.println( item );
        }
        //System.out.println( database.getTable("gakusei").getInsertTable().toString() );
        
    }
}
