package com.example.downloadfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    KeyguardManager keyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        ApplicationUtil.getInstance().addActivity(MainActivity.this);
    }

    public void uploadData(View view) {
        String url = "http://192.168.43.18:8080/saveRes.php";
        String data = "{\"survey_id\":\"324943321\",\"answers\":\"使用真机测试了\"}";
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(data);
            UploadUtil.upload(url, jsonobj, MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("fail json", e.getMessage());
        }
    }

    //退出按钮
    public void exit(View view) {
        //解锁
        LockUtil.getInstance().unLock(this,MainActivity.this);
    }
    //返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            LockUtil.getInstance().unLock(this,MainActivity.this);
        }
        return false;
    }


    //监听解锁响应
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            // 解锁成功
            if (resultCode == RESULT_OK) {
                //关闭程序
                Toast.makeText(this,"Exit the APP",Toast.LENGTH_SHORT).show();
                ApplicationUtil.getInstance().exit();
            } else {
                // 解锁失败或者取消解锁
                Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
