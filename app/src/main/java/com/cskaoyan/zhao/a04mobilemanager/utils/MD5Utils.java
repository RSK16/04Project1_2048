package com.cskaoyan.zhao.a04mobilemanager.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhao on 2016/8/6.
 */

//MD5 hash 算法  又被称为 消息摘要算法--》任意的长度的输入，都会生成一个16字节的hash值。而且 不同的输入算的hash值不一样


public class MD5Utils {

    private static final String TAG = "MD5Utils";

    public static String getMD5(String pwd){

        String md5hashValue="";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            StringBuffer sb= new StringBuffer();
            //计算md5数值 0F
            byte[] digest1 = digest.digest(pwd.getBytes());
            for (byte b:digest1) {

                int i= b&0x000000FF;

                String s = Integer.toHexString(i);

                if (s.length()==1) {
                    sb.append("0");
                }
                sb.append(s);
            }


            md5hashValue=sb.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return  md5hashValue;

    }


    public  static String getFileMD5(String filepath){


        File file =new File(filepath);
        StringBuffer sb=new StringBuffer();
        String md5hashValue="";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            FileInputStream fileInputStream=new FileInputStream(file);

            byte[] bs=new byte[1024];
            int len =-1;

            while((len=fileInputStream.read(bs, 0, 1024))!=-1){

                digest.update(bs, 0, len);

            }

            //文件的MD5值
            byte[] digest2 = digest.digest();

            for (byte b:digest2) {

                int i= b&0x000000FF;
                //FF
                //FFFFFFFf

                String s = Integer.toHexString(i);  //加盐

                if (s.length()==1) {
                    sb.append("0");
                }


                //System.out.println(s);
                sb.append(s);
            }


            md5hashValue=sb.toString();
            System.out.println(md5hashValue);



        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return md5hashValue;

    }
}
