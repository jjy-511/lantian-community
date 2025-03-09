package com.jjynowcoder.community;

import com.jjynowcoder.community.config.AlphaConfig;
import com.jjynowcoder.community.dao.AlphaDao;
import com.jjynowcoder.community.dao.DiscussPostMapper;
import com.jjynowcoder.community.dao.UserMapper;
import com.jjynowcoder.community.entity.DiscussPost;
import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.service.AlphaService;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

     @Test
    public void testSelectUser(){
    User user= userMapper.selectById(101);
    System.out.println(user);

    user=userMapper.selectByName("guanyu");
        System.out.println(user);

        user=userMapper.selectByEmail("nowcoder149@sina.com");
        System.out.println(user);

}

@Test
    public void testInsertUser(){
        User user=new User();
        user.setUsername("test");
        user.setPassword("888666");
        user.setSalt("6888");
        user.setEmail("test@qq.com");
user.setCreateTime(new Date());
int row=userMapper.insertUser(user);
System.out.println(row);
System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser(){
        int row=userMapper.updateStatus(150,1);
        System.out.println(row);

        row=userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(row);

        row=userMapper.updatePassword(150,"666666");
        System.out.println(row);
    }

    @Test
    public void testDiscussPostSelect() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 20);
        for (DiscussPost d : list) System.out.println(d);

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
     }
}
