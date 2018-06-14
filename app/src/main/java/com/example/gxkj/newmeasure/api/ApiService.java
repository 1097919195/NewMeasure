package com.example.gxkj.newmeasure.api;



import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.example.gxkj.newmeasure.bean.MeasureCustomer;
import com.example.gxkj.newmeasure.bean.UserData;
import com.example.gxkj.newmeasure.bean.MeasureWeChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
//        https://www.jianshu.com/p/7687365aa946
//@Path： URL中有参数,如：
//        http://102.10.10.132/api/Accounts/{accountId}
//@Query：参数在URL问号之后,如：
//        http://102.10.10.132/api/Comments ? access_token={access_token}
//@QueryMap：相当于多个@Query
//@Field：用于POST请求，提交单个数据（不显示在网址中）
//@Body： 相当于多个@Field，以对象的形式提交
// Tip1
//        使用@Field时记得添加@FormUrlEncoded
// Tip2
//        若需要重新定义接口地址，可以使用@Url，将地址以参数的形式传入即可
public interface ApiService {

//    @GET("login")
//    Observable<BaseRespose<User>> login(@Query("username") String username, @Query("password") String password);
//
//    //新闻详情
//    @GET("nc/article/{postId}/full.html")
//    Observable<Map<String, NewsDetail>> getNewDetail(
//            @Header("Cache-Control") String cacheControl,//添加响应头（缓存的方式）
//            @Path("postId") String postId);
//
//    //新闻列表
//    //http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
//    @GET("nc/article/{type}/{id}/{startPage}-20.html")
//    Observable<Map<String, List<NewsSummary>>> getNewsList(
//            @Header("Cache-Control") String cacheControl,
//            @Path("type") String type, @Path("id") String id,
//            @Path("startPage") int startPage);
//
//    @GET
//    Observable<ResponseBody> getNewsBodyHtmlPhoto(
//            @Header("Cache-Control") String cacheControl,
//            @Url String photoPath);
//    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
//    // baseUrl 需要符合标准，为空、""、或不合法将会报错
//
//
//    @GET("data/福利/{size}/{page}")
//    Observable<GirlData> getPhotoList(
//            @Header("Cache-Control") String cacheControl,
//            @Path("size") int size,
//            @Path("page") int page);
//
//    //视频
//    @GET("nc/video/list/{type}/n/{startPage}-10.html")
//    Observable<Map<String, List<VideoData>>> getVideoList(
//            @Header("Cache-Control") String cacheControl,
//            @Path("type") String type,
//            @Path("startPage") int startPage);

    /**
     * Test Api
     */

//    @GET("clo/quality")
//    Observable<QualityData> getQuality(
//            @Query("id") String id
//    );
//
//    @FormUrlEncoded
//    @POST("clo/compare")
//    Observable<RetQuality> getUpLoadAfterChecked(
//            @Field("list") Object[][] qualityDataList
//    );

//    //客户端修改密码
//    @FormUrlEncoded
//    @POST("api/change/password")
//    Observable<HttpResponse> changePassword(
//            @Field("old_password") String old_password,
//            @Field("new_password") String new_password
//    );

    /**
     * Release Api
     */

//    //质检项目
//    @GET("api/qc/itemsingle/{id}")
//    Observable<HttpResponse<QualityData>> getQuality(
//            @Path("id") String id
//    );
//

    //获取样衣详情
//    @GET("api/third/samples/parts")
//    Observable<HttpResponse<SampleData>> getSampleData(
//            @Query("content") String id
//    );
//
//    //获取员工详情
//    @GET("api/third/staff/parts")
//    Observable<HttpResponse<StaffData>> getStaffData(
//            @Query("content") String id
//    );
//
//    //绑定成衣卡号
//    @FormUrlEncoded
//    @POST("api/third/samples/bind_card")
//    Observable<HttpResponse> bindingCardWithSample(
//            @Field("card_num") String num,
//            @Field("clothes_id") String id
//    );
//
//    //绑定员工卡号
//    @FormUrlEncoded
//    @POST("api/third/staff/bind_card")
//    Observable<HttpResponse> bindingCardWithStaff(
//            @Field("card_num") String num,
//            @Field("staff_id") String id
//    );
//
    //登录
    @FormUrlEncoded
    @POST("api/client/login")
    Observable<HttpResponse<LoginTokenData>> getTokenWithSignIn(
            @Field("mobile") String username,
            @Field("password") String password
    );

    //获取自己的信息
    @POST("api/client/me")
    Observable<HttpResponse<UserData>> getUser(
    );

    //修改合同号判断
    @GET("api/client/measure_contracts/{id}")
    Observable<HttpResponse<ContractNumWithPartsData>> changeContractNum(
            @Path("id") String id
    );

    //合同成员的部位获取显示
    @FormUrlEncoded
    @POST("api/client/measurement_user")
    Observable<HttpResponse<MeasureCustomer>> MeasureCustomer(
            @Field("tid") String tid
    );

    //微信用户的部位获取显示
    @GET("api/client/get_info_with_open_id/{openID}")
    Observable<HttpResponse<MeasureWeChat>> MeasureWeChat(
            @Path("openID") String openID
    );

    //上传量体的数据
    @Multipart//一定要一个部位空的part参数
    @POST("api/client/measurements")
    Observable<HttpResponse> upLoadMeasureResult(
            @Query("tid") String tid,
            @Query("openID") String openID,
            @Part("sex") int sex,
            @Part MultipartBody.Part[] images,
            @Query("data") String data,
            @Query("contract_id") String contract_id
    );

    //客户端修改密码
    @FormUrlEncoded
    @POST("api/client/reset")
    Observable<HttpResponse> changePassword(
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );


}
