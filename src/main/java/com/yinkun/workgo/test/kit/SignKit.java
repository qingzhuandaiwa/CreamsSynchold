package com.yinkun.workgo.test.kit;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SignKit {

    public static String getSign(Map<String, String> map, String key){
        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            // 参数值为空，则不参与签名
            sb.append(k).append("=").append(map.get(k)).append("&");
        }
        return getMd5(sb.substring(0, sb.length()-1)+"&key=" + key).toUpperCase();
    }

    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString().toUpperCase();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUUID32(){
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return uuid;
        //  return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
