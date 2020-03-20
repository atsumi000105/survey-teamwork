package mg.studio.android.survey;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChooseActivity extends AppCompatActivity {
    Intent CtoR;
    TextView SquesNUM;
    TextView MquesNUM;
    TextView EquesNUM;
    TextView Squs;
    TextView Mqus;
    TextView Equs;
    RadioGroup Soption;
    RadioGroup Moption;
    EditText Eoption;
    String style;
    String saveAnswer;
    List<String> quesList;
    int count;
    int current;
    JSONArray JArr;
    JSONObject Jo;
    String Jstr="";
    String Survey_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        quesList=new ArrayList();
        style="";
        saveAnswer="{";
        CtoR=new Intent(this,ReportActivity.class);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        ApplicationUtil.getInstance().addActivity(ChooseActivity.this);
    }


    //scan code
    public void StartQRSurvey(View view) throws IOException {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        //scan
        intentIntegrator.initiateScan();
    }
    //get result
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"Cancel the scan",Toast.LENGTH_LONG).show();
            }else {
                Jstr=result.getContents();
                if(Jstr.length()!=0){
                    try {
                        JSONObject Jsurvey=new JSONObject(Jstr);
                        Jstr=Jsurvey.getString("survey");
                        Jsurvey=new JSONObject(Jstr);
                        Survey_id=Jsurvey.getString("id");
                        Jstr=Jsurvey.getString("questions");
                        JArr = new JSONArray(Jstr);
                        count=JArr.length();
                        current=0;
                        Jo=(JSONObject) JArr.get(0);
                        if(Jo.getString("type").equals("single")){
                            ShowSingle();
                        }
                        else if(Jo.getString("type").equals("multiple")){
                            ShowMultiple();
                        }
                        else if(Jo.getString("type").equals("edittext")){
                            ShowEdit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //single questions
    private void ShowSingle() throws JSONException {
        style="single";
        int quesnum=current+1;
        String ques=Jo.getString("question");
        String option=Jo.getString("options");
        JSONArray Joption=new JSONArray(option);
        setContentView(R.layout.single_question);
        SquesNUM=(TextView)findViewById(R.id.SquesionNum);
        Squs=(TextView)findViewById(R.id.Squesiontext);
        Soption=(RadioGroup)findViewById(R.id.SOption);
        Soption.removeAllViewsInLayout(); //清空原有选项
        SquesNUM.setText("Question "+quesnum);
        Squs.setText(ques);
        quesList.add(ques);
        for(int i=0;i<Joption.length();i++){
            RadioButton rb=new RadioButton(this);
            Soption.addView(rb);
            rb.setText(Joption.getJSONObject(i).getString(""+(i+1)));
        }
    }

    //multiple questions
    private void ShowMultiple() throws JSONException {
        style="multiple";
        int quesnum=current+1;
        String ques=Jo.getString("question");
        String option=Jo.getString("options");
        JSONArray Joption=new JSONArray(option);
        setContentView(R.layout.multiple_question);
        MquesNUM=(TextView)findViewById(R.id.MquesionNum);
        Mqus=(TextView)findViewById(R.id.Mquesiontext);
        Moption=(RadioGroup)findViewById(R.id.MOption);
        Moption.removeAllViewsInLayout(); //清空原有选项
        MquesNUM.setText("Question "+quesnum);
        Mqus.setText(ques);
        quesList.add(ques);
        for(int i=0;i<Joption.length();i++){
            CheckBox cb=new CheckBox(this);
            Moption.addView(cb);
            cb.setText(Joption.getJSONObject(i).getString(""+(i+1)));
        }
    }

    //filling questions
    private void ShowEdit() throws JSONException {
        style="edittext";
        int quesnum=current+1;
        String ques=Jo.getString("question");
        setContentView(R.layout.edit_question);
        EquesNUM=(TextView)findViewById(R.id.EquesionNum);
        Equs=(TextView)findViewById(R.id.Equesiontext);
        Eoption=(EditText) findViewById(R.id.EOption);

        Eoption.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Eoption.setGravity(Gravity.TOP);
        Eoption.setSingleLine(false);
        Eoption.setHorizontallyScrolling(false);

        EquesNUM.setText("Question "+quesnum);
        Equs.setText(ques);
        quesList.add(ques);
    }

    //next question
    public void nextque(View view) throws JSONException {
        Boolean flag=false;
        if(style.equals("single")){
            flag=saveSingleAnswer();
        }else if(style.equals("multiple")){
            flag=saveMultipleAnswer();
        }else if(style.equals("edittext")){
            flag=saveEditAnswer();
        }

        if(flag){//答案已保存
            if(current+1<count){//还未到最后一个问题
                current++;
                saveAnswer+=",";
                Jo=(JSONObject) JArr.get(current);
                if(Jo.getString("type").equals("single")){
                    ShowSingle();
                }
                else if(Jo.getString("type").equals("multiple")){
                    ShowMultiple();
                }
                else if(Jo.getString("type").equals("edittext")){
                    ShowEdit();
                }
            }else{
                saveAnswer+="}";
                setContentView(R.layout.finish_jsonsurvey);
            }
        }else{
            Toast.makeText(this, "please answer the question", Toast.LENGTH_SHORT).show();
        }
    }

    //save single
    private Boolean saveSingleAnswer() {
        for (int i = 0; i < Soption.getChildCount(); i++) {
            RadioButton rb = (RadioButton) Soption.getChildAt(i);
            if (rb.isChecked()) {

                saveAnswer += "\"question "+(current+1)+"\":\"" + rb.getText().toString() + "\"";
                return true;
            }
        }
        return false;
    }

    //save multiple
    private Boolean saveMultipleAnswer() {
        String str = "";
        int count = 0;
        for (int i = 0; i < Moption.getChildCount(); i++) {
            CheckBox cb = (CheckBox) Moption.getChildAt(i);
            if (cb.isChecked()) {
                count++;
                str += cb.getText() + ";";
            }
        }

        saveAnswer += "\"question "+(current+1)+"\":\"" + str + "\"";
        if (count != 0)
        {return true;}else{
            return false;
        }
    }
    //save edit
    private Boolean saveEditAnswer() {
        String str = Eoption.getText().toString();
        if (str.length() != 0) {

            saveAnswer += "\"question "+(current+1)+"\":\"" + str + "\"";
            return true;
        } else{
        return false;}
    }


    //jump to finish
    public void FinishJS(View view) throws IOException {
        CtoR.putExtra("answerJSON",saveAnswer);
        CtoR.putExtra("count",count);
        CtoR.putExtra("survey_id",Survey_id);
        String[] quesArr=quesList.toArray(new String[quesList.size()]);
        CtoR.putExtra("quesArr",quesArr);

        startActivity(CtoR);
    }

}
