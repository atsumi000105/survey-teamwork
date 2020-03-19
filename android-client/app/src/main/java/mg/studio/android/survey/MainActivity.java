package mg.studio.android.survey;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Intent MtoC;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        MtoC = new Intent(MainActivity.this, ChooseActivity.class);
        ApplicationUtil.getInstance().addActivity(MainActivity.this);
    }


    //Users can enter the questionnaire survey after confirming the terms
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void start(View view) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        CheckBox ac = (CheckBox) findViewById(R.id.accept);
        if (ac.isChecked()) {
            startActivity(MtoC);
        } else {
            AlertDialog accept = new AlertDialog.Builder(this)
                    .setMessage("Please confirm that you accept the requests before answering questions")
                    .setPositiveButton("OK", null)
                    .create();
            accept.show();
        }
    }
}


