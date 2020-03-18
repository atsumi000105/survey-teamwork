package mg.studio.android.survey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建Survey_Result表
        String sql = "create table Survey_Result(id INTEGER  primary key  autoincrement,survey_id  varchar(10) not null ,survey_data varchar(1000)," +
                "location varchar(30), time varchar(30),imei varchar(30))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
