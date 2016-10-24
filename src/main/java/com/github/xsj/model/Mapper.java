package com.github.xsj.model;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;


public interface Mapper {

    User getUser(long id);

    void insertUser(User user);

    @Update("update users set name = #{name}")
    void updateUser(User user);

    @Delete("delete from users where id = #{id}")
    void deleteUser(long id);

}
