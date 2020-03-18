package mg.studio.android.survey;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;


//上传工具类，调用方法：UploadUtil.upload(params);
//在使用改文件时，需要注意其他联网相关的部分：
// Manifest文件：用户联网权限；新增配置文件地址引入；新增配置文件：res/xml/ 文件
public class UploadUtil{
    //上传
    public static void upload(final String url, final JSONObject jsonobj, final Context context){
        //判断网络是否打开
        ConnectivityManager manager=(ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if(networkInfo==null||!networkInfo.isAvailable()){
                Toast.makeText(context,"Please connect to network and ensure you phone and computer in the same LAN",Toast.LENGTH_LONG).show();
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UploadUtil.uploadData(url,jsonobj,context);
                    }
                }).start();
            }
        }

    }
    //上传数据
    private static void uploadData(String serverUrl, JSONObject jsonobj, Context context){
        try{
            //将参数连接到stringBuilder中
            StringBuilder stringBuilder=new StringBuilder();
            Iterator it=jsonobj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = jsonobj.getString(key);
                stringBuilder.append(key).append("=").append(value);
                stringBuilder.append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);//删除最后一个&
            String data=stringBuilder.toString();

            //网络连接
            URL url=new URL(serverUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            OutputStream out=new DataOutputStream(conn.getOutputStream()) ;
            out.write(data.getBytes());
            out.close();
            if(conn.getResponseCode()==200){

                // 获取响应的输入流对象
                System.out.println(conn.getContent());
                InputStream in=conn.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
//                while ((line=reader.readLine())!=null){
//                    Log.d("success",line);
//                }
                if((line=reader.readLine())!=null){
                    Looper.prepare();
                    Toast.makeText(context,"success to upload",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    Looper.prepare();
                    Toast.makeText(context,"failed!",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception e){
            e.printStackTrace();
            Looper.prepare();//子线程不能直接toast
            Toast.makeText(context,"fail to upload,please ensure your computer and phone on the same network",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }



    //上传文件：未经测试
    private void uploadFile(String serverUrl, String filepath){
        try{
            //创建对象
            URL url=new URL(serverUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();

            //允许输入输出流,不使用缓冲
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            //设置请求头
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection","Keep-Alive");
            //conn.setRequestProperty("Content-Type","multipart/form-data;");
            conn.setRequestProperty("Charset","UTF-8");
            conn.setReadTimeout(5000);//设置读取超时
            conn.setConnectTimeout(10000);//设置连接网络超时

            //获取输出流，用于向服务器写数据
            OutputStream out=new DataOutputStream(conn.getOutputStream());

            //获取文件输入流
            FileInputStream fileIn=new FileInputStream(filepath);
            //设置每次写入的bytes数
            int bfsize=1024;
            byte[] bf=new byte[bfsize];
            int fileLen=-1;//文件长度

            //写入文件到输出流
            while((fileLen=fileIn.read(bf))!=-1){
                out.write(bf,0,fileLen);
            }
            out.flush();
            fileIn.close();
            out.close();

            //判断上传成功
            if(conn.getResponseCode()==200){
                Log.d("success",filepath+" 文件上传成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("fail",filepath+" 文件上传失败");
            Log.e("upload fail",e.toString());
        }
    }


}
