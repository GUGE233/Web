package com.example.guge.web;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //配置相应的OKHttp对象
    private static OkHttpClient createOKHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    //构造Retrofit对象
    private static Retrofit createRetrofit(String baseurl){
        return new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOKHttp())
                .build();
    }

    final List<Information> itemlist = new ArrayList<Information>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //首先把原型进度条设置为不可见的
        final ProgressBar bar1 = (ProgressBar)findViewById(R.id.bar1);
        bar1.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final RecAdapter recAdapter = new RecAdapter(this,R.layout.item,itemlist) {
            @Override
            public void convert(ViewHolder holder, Information info) {
                TextView username = holder.getView(R.id.username);
                TextView id = holder.getView(R.id.id);
                TextView blog = holder.getView(R.id.blog);
                username.setText(info.getLogin());
                id.setText(String.valueOf(info.getId()));
                blog.setText(info.getBlog());
            }
        };
        //设置列表的点击事件
        recAdapter.setOnItemClickListener(new RecAdapter.OnItemClickListener() {
            @Override
            //点击列表中的项
            public void onClick(int position) {
                String user = itemlist.get(position).getLogin();
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }

            //长按弹出确认对话框，确认后删除
            @Override
            public void onLongClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确认要删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemlist.remove(position);
                        recAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么也不做
                    }
                });
                builder.show();
            }
        });



        recyclerView.setAdapter(recAdapter);


        //按下fetch按钮时进行的操作
        Button btn_fetch = (Button)findViewById(R.id.btn_fetch);
        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //一旦按下按钮查询的时候，圆形进度条就显示并转动
                bar1.setVisibility(View.VISIBLE);
                bar1.setProgress(0);
                bar1.setSecondaryProgress(80);
                EditText editText = (EditText) findViewById(R.id.search_input);
                String name = editText.getText().toString();
                String baseurl = "https://api.github.com";
                Retrofit retrofit = createRetrofit(baseurl);
                GithubService service = retrofit.create(GithubService.class);
                service.getUser(name)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Information>() {
                            @Override
                            public void onCompleted() {
                                System.out.println("完成传输");
                                //完成操作后圆形进度条再次被隐藏
                                bar1.incrementProgressBy(80);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                                //完成操作后圆形进度条再次被隐藏
                                bar1.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onNext(Information information) {
                                itemlist.add(information);
                                bar1.incrementSecondaryProgressBy(20);
                                recAdapter.notifyItemInserted(itemlist.size()-1);
                                //完成操作后圆形进度条再次被隐藏
                                bar1.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });

        //按下clear按钮时进行的清空操作
        Button btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.search_input);
                editText.setText("");
            }
        });





    }
}
