package com.thirdpart.network;

import com.thirdpart.entity.EntityBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by qzzhu on 16-10-19.
 */

public class HttpMethods {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private static HttpMethods instance = null;

    private Retrofit retrofit = null;

    private HttpMethods(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static HttpMethods getInstance(){
        if(instance == null)
        {
            synchronized (HttpMethods.class)
            {
                if(instance == null)
                    instance = new HttpMethods();
            }
        }
        return instance;
    }

    public void request(Subscriber<EntityBean> subscribe,int start,int count){
        MoviceService service = retrofit.create(MoviceService.class);

        //生成一个Observable对象
        Observable<EntityBean> observable = service.getTopMovieByAdapter(start,count);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe);
    }
}
