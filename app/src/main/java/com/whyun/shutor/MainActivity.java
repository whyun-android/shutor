package com.whyun.shutor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建确认框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Shutdown Confirmation");
        builder.setMessage("The device will shut down in 3 seconds.");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消倒计时并退出程序
                countDownTimer.cancel();
                finish();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

        // 启动倒计时
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新一次
                alertDialog.setMessage("The device will shut down in " + millisUntilFinished / 1000 + " seconds.");
            }

            @Override
            public void onFinish() {
                // 倒计时结束，执行关机命令
                shutdownDevice();
            }
        }.start();
    }

    private void shutdownDevice() {
        try {
            // 需要 root 权限
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            // 执行关机命令
            outputStream.writeBytes("reboot -p\n");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();

            su.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
