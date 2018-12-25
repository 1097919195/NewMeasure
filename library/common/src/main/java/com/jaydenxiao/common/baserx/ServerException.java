package com.jaydenxiao.common.baserx;

/**
 * des:服务器请求异常
 * Created by xsf
 * on 2016.09.10:16
 *
 * 此处最好继承RuntimeException,因为继承Exception需要自己被抛出异常
 */
public class ServerException extends Exception{

    public ServerException(String msg){
        super(msg);
    }

}
