package com.wcaaotr.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author Connor
 * @create 2021-06-20-19:21
 */
public class CommunityUtil {

    /**
     * 生成随机字符串
     * @return 随机字符串
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 对密码进行 MD5 加密
     * @param key 输入的密码 + salt 拼接而成
     * @return 加密后的 16 进制串
     */
    public static String md5(String key){
        if(StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
