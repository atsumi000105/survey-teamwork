package mg.studio.android.survey;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {
    TextView showAnswer;
    Intent getAnswer;
    int count;
    LocationManager lm;
    String DATA_TIME;
    String LOCATION;
    String SURVEY_ID;
    String IMEI;
    String ANSWER;
    String []quesArr;
    DatabaseOperator dataOP;
    EditText IP;
    //Permissions
    private static String[] PERMISSION = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static int PERMISSION_CODE = 1;

    //get permissions
    public void getPermission(){
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)||(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)||(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)){
            AlertDialog accept = new AlertDialog.Builder(this)
                .setMessage("We need to get your permisson of: \n\n    READ_PHONE_STATE, \n    ACCESS_FINE_LOCATION, \n    ACCESS_COARSE_LOCATION" +
                        "\n\nso that we can get your IMEI and your location. We need these information to add to database")
                .setPositiveButton("OK", null)
                .create();
        accept.show();}
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_CODE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getAnswer=getIntent();
        count=getAnswer.getIntExtra("count",0);
        ANSWER=getAnswer.getStringExtra("answerJSON");
        SURVEY_ID=getAnswer.getStringExtra("survey_id");
        quesArr=getAnswer.getStringArrayExtra("quesArr");//get questions
        dataOP=new DatabaseOperator(this);
        try {
            ShowAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showAnswer.setMovementMethod(ScrollingMovementMethod.getInstance());
        ApplicationUtil.getInstance().addActivity(ReportActivity.this);

    }

    //show the answer
    public void ShowAnswer() throws JSONException {
        getAnswer=getIntent();
        JSONObject answer=new JSONObject(ANSWER);
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<=count;i++){
            String answer_text=answer.getString("question "+i);
//            sb.append("question"+i+":"+answer_text+"\n");
            sb.append(quesArr[i-1]+"\n"+answer_text+"\n\n");
            System.out.println(quesArr[i-1]);
        }
        showAnswer= (TextView)findViewById(R.id.showAnswers);
        showAnswer.setText(sb.toString());
    }

    //upload

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void UPLOAD(View view) throws IOException {
        getPermission();
       /* save to local database;*/
        try {
            String[] imei=getIMEI();
            IMEI=imei[0]+","+imei[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOCATION=getLocation();
        DATA_TIME=getTime();
        dataOP.add(SURVEY_ID,ANSWER,LOCATION,DATA_TIME,IMEI);
        /*String result=dataOP.find();
        showAnswer.setText(result);*/


        /*update to the server*/
        IP=findViewById(R.id.ip_computer);
        String IP_computer=IP.getText().toString();
        String url="http://"+IP_computer+"/saveRes.php";
        JSONObject jsonobj=new JSONObject();
        String ans=ANSWER.replaceAll("\"","");
        try{
            jsonobj.put("survey_id",SURVEY_ID);
            jsonobj.put("answers",ans);
            jsonobj.put("location",LOCATION);
            jsonobj.put("time",DATA_TIME);
            jsonobj.put("imei",IMEI);
            UploadUtil.upload(url,jsonobj,ReportActivity.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("wrap data in json",e.getMessage());
            Toast.makeText(this,"wrap wrong",Toast.LENGTH_SHORT).show();
        }

    }

    //exit the app
    public void EXIT(View view){
        LockUtil.getInstance().unLock(this,ReportActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // unlock success
            if (resultCode == RESULT_OK) {
                //exit
                Toast.makeText( this, "Exit the APP", Toast.LENGTH_SHORT).show();
                ApplicationUtil.getInstance().exit();
            } else {
                // unlock failed
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //save file in database
    public void saveResultFile(String msg) throws IOException {
        File sdFile = Environment.getExternalStorageDirectory();
        File result = new File(sdFile, "result.json");
        int i=0;
        while (result.exists()) {
            i++;
            result=new File(sdFile,"result"+i+".json");
        }
        try {
            FileOutputStream fout = new FileOutputStream(result);
            fout.write(msg.getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "Result has been saved in"+result.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //get imei
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] getIMEI() throws Exception {
        String[] imei=new String[]{"null","null"};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission of getting phone state is not allowed", Toast.LENGTH_SHORT).show();
            return imei;
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            //获取第一个imei
            try{
                Method method = tm.getClass().getMethod("getImei", int.class);
                imei[0] = (String) method.invoke(tm, 0);
            }catch (Exception e){
                return imei;
            }
            //get the second imei(if have one)
            try{
                Method method = tm.getClass().getMethod("getImei", int.class);
                imei[1] = (String) method.invoke(tm, 1);
            }catch (Exception e){
                return imei;
            }
            return imei;
        }
    }

    //get timestamp
    public String getTime() {
        double time = System.currentTimeMillis();
        String TIME=String.valueOf(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date)+TIME;
    }

    //get location
    public String getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission of getting location information is not allowed", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            Location location=null;
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                        }
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }
                        @Override
                        public void onProviderEnabled(String provider) {
                        }
                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    }
            );
            location = lm.getLastKnownLocation((LocationManager.GPS_PROVIDER));

            if (location != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("longitude：");
                sb.append(location.getLongitude());
                sb.append(", latitude：");
                sb.append(location.getLatitude());
                return sb.toString();
            } else {
                Toast.makeText(this, "Location information not obtained", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }
}
