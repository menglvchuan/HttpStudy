package com.bawei.okhttp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.okhttp.Bean.RegistBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Time:2019/2/13
 * <p>
 * Author:肖佳莹
 * <p>
 * Description:
 */
public class ZhuActivity extends AppCompatActivity {
    private String path = "http://172.17.8.100/small/user/v1/register";
    private TextView num, pwd, return_btu;
    private Button regist;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String json = (String) msg.obj;
                Gson gson = new Gson();
                RegistBean registBean = gson.fromJson(json, RegistBean.class);
                if (registBean.getStatus().equals("0000")) {
                    Toast.makeText(ZhuActivity.this, registBean.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ZhuActivity.this, MainActivity.class));
                    finish();
                } else if (registBean.getStatus().equals("1001")) {
                    Toast.makeText(ZhuActivity.this, registBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        //初始化控件
        initView();
        initData();
    }
    private void initView() {
        //找控件
        num = findViewById(R.id.num_regist);
        pwd = findViewById(R.id.pwd_regist);
        return_btu = findViewById(R.id.return_regist);
        regist = findViewById(R.id.regist_regist);
        //返回按钮点击监听
        return_btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ZhuActivity.this, MainActivity.class));
                finish();
            }
        });

    }


    private void initData() {
        //立即注册按钮点击监听
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取手机号和密码
                String phone = num.getText().toString();
                String password = pwd.getText().toString();
                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ZhuActivity.this, "手机号或密码不能为空!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (phone.length() != 11) {
                        Toast.makeText(ZhuActivity.this, "手机号格式不正确!!!", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 3) {
                        Toast.makeText(ZhuActivity.this, "密码格式不正确!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        //实例化okhttp
                        OkHttpClient okHttpClient = new OkHttpClient();
                        //请求体
                        RequestBody formBody = new FormBody.Builder()
                                .add("phone", phone)
                                .add("pwd", password)
                                .build();
                        //配置请求方式&url
                        Request request = new Request.Builder()
                                .post(formBody)
                                .url(path)
                                .build();
                        //执行异步
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //获取到数据
                                String json = response.body().string();
                                Log.i("xxx", "onResponse: " + json);
                                //创建消息对象
                                Message message = new Message();
                                message.what = 0;
                                message.obj = json;
                                //发送消息
                                handler.sendMessage(message);
                            }
                        });
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
