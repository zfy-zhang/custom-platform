package org.elisha.custom.repository;

import org.elisha.custom.domain.User;
import org.elisha.orm.annotation.Param;
import org.elisha.orm.annotation.Read;
import org.elisha.orm.annotation.Write;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface UserMapper {

    @Write("INSERT INTO users( name,password,email,phone_number) "
            +"VALUES({name},{password},{email},{phoneNumber})")
    int insertUser(User user);


    @Write("UPDATE users SET password = {password} where id = {id}")
    int update(User user);


    @Read("SELECT * FROM users ")
    List<User> queryAll();


    @Read("SELECT * FROM users where id = {id}")
    User get(@Param("id") long id);

}

