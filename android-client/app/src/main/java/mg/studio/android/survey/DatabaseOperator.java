package mg.studio.android.survey;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

public class DatabaseOperator {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseOperator(Context context) {
        dbHelper = new DatabaseHelper(context, "Survey_Result", null, 1);
        db = dbHelper.getWritableDatabase();
        System.out.println("建立数据库");
    }

//    // 检验survey_id是否已存在于Survey_Result
//    public boolean isSurveyIdAlreadyExisted(String value) {
//        String Query = "Select * from Survey_Result where survey_id =?";
//        Cursor cursor = db.rawQuery(Query, new String[] { value });
//        if (cursor.getCount() > 0) {
//            cursor.close();
//            return true;
//        }
//        cursor.close();
//        return false;
//    }







    // 添加id,survey_id,survey_data,location,time,emei
    public void add(String survey_id,String survey_data,String location,String time,String imei) {
        db.execSQL("insert into Survey_Result(survey_id,survey_data,location,time,imei) values(?,?,?,?,?)",
                new Object[] {survey_id,survey_data,location,time,imei });
    }


    //显示所有数据库中的信息
   /* public String find(){
        String result="";
        Cursor cursor=db.query("Survey_Result",null,null,null,null,null,null,null);
        if(cursor.getCount()==0){
          return "no data";
        }
        else{
            cursor.moveToFirst();
            result= "id:"+cursor.getString(0)+"\n"+
                    "survey_id:"+cursor.getString(1)+"\n"+
                    "survey_data:"+cursor.getString(2)+"\n"+
            "location:"+cursor.getString(3)+"\n"+
                    "time:"+cursor.getString(4)+"\n"+
                    "imei:"+cursor.getString(5)+"\n";
        }while(cursor.moveToNext()){
            result+= "id:"+cursor.getString(0)+"\n"+
                    "survey_id:"+cursor.getString(1)+"\n"+
                    "survey_data:"+cursor.getString(2)+"\n"+
                    "location:"+cursor.getString(3)+"\n"+
                    "time:"+cursor.getString(4)+"\n"+
                    "imei:"+cursor.getString(4)+"\n";
        }
        cursor.close();
        return result;
    }*/

}
