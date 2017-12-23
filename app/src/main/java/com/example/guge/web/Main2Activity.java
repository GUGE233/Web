package com.example.guge.web;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //圆形进度条相关
        final ProgressBar bar = (ProgressBar)findViewById(R.id.bar2);
        bar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String user = "";
        if(intent!=null){
            user = intent.getStringExtra("user");
        }

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));






        String baseurl = "https://api.github.com";
        Retrofit retrofit = createRetrofit(baseurl);
        reposService service = retrofit.create(reposService.class);
        service.getUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<repos>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("完成传输");


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Main2Activity.this,"查询失败",Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(List<repos> reposList) {
                        final RecAdapter2 recAdapter2 = new RecAdapter2(Main2Activity.this,R.layout.item2,reposList) {
                            @Override
                            public void convert(ViewHolder holder, repos rep) {
                                TextView name = holder.getView(R.id.name2);
                                TextView language = holder.getView(R.id.language2);
                                TextView description = holder.getView(R.id.description2);
                                name.setText(rep.getName());
                                language.setText(rep.getLanguage());
                                description.setText(rep.getDescription());
                            }
                        };
                        recyclerView.setAdapter(recAdapter2);

                        //圆形进度条消失
                        bar.setVisibility(View.GONE);
                    }
                });

    }
}
