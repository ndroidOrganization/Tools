package com.thirdpart.network;

import com.thirdpart.entity.EntityBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by qzzhu on 16-10-19.
 */

public interface MoviceService {

    //uri = https://api.douban.com/v2/movie/top250?start=0&count=10

    @GET("top250")
    Call<EntityBean> getTopMovie(@Query("start") int start,@Query("count") int count);

    /**
     * 动态修改uri中的{type}变量
     * @param type 需要改变的路径
     * @param start 参数1
     * @param count 参数2
     * @return 返回一个Call(来自于OKHTTP)
     */
    @GET("{type}/top250")
    Call<EntityBean> getTopTypes(@Path("type") String type, @Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<EntityBean> getTopMovieByAdapter(@Query("start") int start, @Query("count") int count);
}
