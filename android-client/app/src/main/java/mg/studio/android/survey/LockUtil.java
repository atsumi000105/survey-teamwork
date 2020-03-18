package mg.studio.android.survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;

import static android.app.Activity.RESULT_OK;

//解锁工具，调用方法:LockUtil.getInstance().unLock(params...)
public class LockUtil{
    Context context;
    Activity activity;
    private static LockUtil instance;

    //获取实例对象
    public static LockUtil getInstance(){
        if(instance==null){
            instance=new LockUtil();
        }
        return instance;
    }
    public void unLock(final Context context, final Activity activity){
        this.setContext(context);
        this.setActivity(activity);
        Dialog dialog=new AlertDialog.Builder(context)
                .setTitle("Exit the APP?")
                .setMessage("Are you sure to Exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {//确认退出，进入解锁
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doUnlock(context,activity);
//                        ApplicationUtil.getInstance().exit();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {//取消退出
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();
    }
    //解锁
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void doUnlock(Context context, Activity activity){
        KeyguardManager keyManager;
        keyManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        //判断设备是否设置解锁密码
        if(!keyManager.isKeyguardSecure()){
            Toast.makeText(context,"You haven't set up a screen lock",Toast.LENGTH_SHORT).show();
        }else{
            //解锁界面的标题和描述
            Intent intent=keyManager.createConfirmDeviceCredentialIntent("Enter Screen Key","You can exit the app until you unlock");
            if(intent!=null){
                activity.startActivityForResult(intent,100);
            }
        }
    }

    public void setContext(Context context){
        this.context=context;
    }
    public void setActivity(Activity activity){
        this.activity=activity;
    }

}
