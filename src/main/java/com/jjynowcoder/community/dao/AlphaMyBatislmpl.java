package com.jjynowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AlphaMyBatislmpl implements AlphaDao{
    @Override
    public String select() {
        return "MyBatis";
    }
}
