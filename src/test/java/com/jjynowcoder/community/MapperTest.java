package com.jjynowcoder.community;

import com.jjynowcoder.community.dao.DiscussPostMapper;
import com.jjynowcoder.community.dao.LoginTicketMapper;
import com.jjynowcoder.community.dao.UserMapper;
import com.jjynowcoder.community.entity.DiscussPost;
import com.jjynowcoder.community.entity.LoginTicket;
import com.jjynowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @Autowired
    private LoginTicketMapper loginTicketMapper;

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

     @Test
     public void testInsertLoginTicket(){
         LoginTicket loginTicket=new LoginTicket();
         loginTicket.setUserId(233);
         loginTicket.setTicket("abcd");
         loginTicket.setStatus(0);
         loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
         loginTicketMapper.insertLoginTicket(loginTicket);
     }
}
