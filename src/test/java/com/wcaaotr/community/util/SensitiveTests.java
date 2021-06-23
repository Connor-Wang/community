package com.wcaaotr.community.util;

import com.wcaaotr.community.CommunityApplication;
import com.wcaaotr.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Connor
 * @create 2021-06-22-16:54
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以#赌※博!，可以#吸4毒&，可以@@嫖####%^$#娼&。敏感词检测。";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
