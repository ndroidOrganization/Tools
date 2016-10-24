package com.thirdpart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thirdpart.entity.EntityBean;
import com.thirdpart.network.MoviceService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static String URL = "https://api.douban.com/v2/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 使用原生的Retrofit进行网络请求
     */
    private void getMoive(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(new OkHttpClient())
                .build();

        MoviceService service = retrofit.create(MoviceService.class);


        //生成一个Call对象
        Call<EntityBean> call = null;
        call =service.getTopTypes("movie",0,10);
        //equals
        call =service.getTopMovie(0,10);

            //Sync
            //call.execute();
        //Async
        call.enqueue(new Callback<EntityBean>() {
            @Override
            public void onResponse(Call<EntityBean> call, Response<EntityBean> response) {

            }

            @Override
            public void onFailure(Call<EntityBean> call, Throwable t) {

            }
        });

    }


    /**
     * 使用RxJava+Retrofit
     */
    private void getMovie(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        MoviceService service = retrofit.create(MoviceService.class);

        //生成一个Observable对象
        Observable<EntityBean> observable = service.getTopMovieByAdapter(0,20);
        observable
                .compose(new Observable.Transformer<EntityBean, EntityBean>() {
                    @Override
                    public Observable<EntityBean> call(Observable<EntityBean> observable) {
                        return observable
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                //equals
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EntityBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EntityBean entityBean) {

                    }
                });


    }
}
