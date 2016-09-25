package top.ljaer.www.phonemanager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LJaer on 16/9/24.
 */
public class MD5Util {
    /*//银行 银行卡 6位数字 将密码进行10-30次MD5加密
    public static void main(String[] args) {
        String password = "123456";
        StringBuilder sb = new StringBuilder();
        try {
            //1、获取数据摘要器
            //arg0:加密的方式
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //2、将一个byte数组进行加密操作,返回的是一个加密过的byte数组,二进制的哈希算法,md5加密的第一步
            byte[] digest = messageDigest.digest(password.getBytes());
            //3、遍历byte数组
            for (int i = 0; i < digest.length; i++) {
                //4、MD5加密
                //byte值 -128-127
                int result = digest[i] & 0xff;
                //将得到的int类型的值,转化为16进制的字符串
                //String hexString = Integer.toHexString(result)+1;//不规则加密,加盐
                String hexString = Integer.toHexString(result);
                if(hexString.length()<2){
//                    System.out.println("0");
                    sb.append("0");
                }
//                System.out.println(hexString);
                sb.append(hexString);
                System.out.println(sb.toString());

            }
        } catch (NoSuchAlgorithmException e) {
            //找不到加密方式的异常
            e.printStackTrace();
        }
    }*/

    /**
     *MD5加密
     * @param password
     * @return
     */
    public static String passwordMD5(String password){
        StringBuilder sb = new StringBuilder();
        try {
            //1、获取数据摘要器
            //arg0:加密的方式
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //2、将一个byte数组进行加密操作,返回的是一个加密过的byte数组,二进制的哈希算法,md5加密的第一步
            byte[] digest = messageDigest.digest(password.getBytes());
            //3、遍历byte数组
            for (int i = 0; i < digest.length; i++) {
                //4、MD5加密
                //byte值 -128-127
                int result = digest[i] & 0xff;
                //将得到的int类型的值,转化为16进制的字符串
                //String hexString = Integer.toHexString(result)+1;//不规则加密,加盐
                String hexString = Integer.toHexString(result);
                if(hexString.length()<2){
//                    System.out.println("0");
                    sb.append("0");
                }
//                System.out.println(hexString);
                sb.append(hexString);
            }
            return  sb.toString();
        } catch (NoSuchAlgorithmException e) {
            //找不到加密方式的异常
            e.printStackTrace();
        }
        return null;
    }
}
