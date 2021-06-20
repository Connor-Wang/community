package com.wcaaotr.community.dao;

import com.wcaaotr.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Connor
 * @create 2021-06-16-8:48
 */
@Mapper
public interface UserMapper {

    User selectById(Integer id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(Integer id, Integer status);

    int updateHeader(Integer id, String headerUrl);

    int updatePassword(Integer id, String password);
}
