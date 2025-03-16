package com.jjynowcoder.community;

import com.jjynowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitive(){
        String text="我是港独，我是台独！我爱赌博、嫖娼、吸毒！你们这群傻逼，我操你妈！";
        text=sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
