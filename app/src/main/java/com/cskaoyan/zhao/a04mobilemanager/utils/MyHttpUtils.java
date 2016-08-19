package com.cskaoyan.zhao.a04mobilemanager.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhao on 2016/8/4.
 */
public class MyHttpUtils {

    public static String getStringFromInputStream(InputStream is){
        String ret="";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024];
        int len =-1;
        try {

            while ((len=is.read(bytes,0,1024))!=-1){
                byteArrayOutputStream.write(bytes,0,len);
            }

           // is.close();
            byteArrayOutputStream.close();


            ret = byteArrayOutputStream.toString("GBK");


        } catch (IOException e) {
            e.printStackTrace();
        }


        return  ret;
    }
}
