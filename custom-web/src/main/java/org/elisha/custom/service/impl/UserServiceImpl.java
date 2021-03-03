package org.elisha.custom.service.impl;


import org.elisha.custom.domain.User;
import org.elisha.custom.repository.UserMapper;
import org.elisha.custom.service.UserService;
import org.elisha.orm.MapperBuild;
import org.elisha.web.mvc.header.annotation.Autowired;
import org.elisha.web.mvc.header.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl() throws ClassNotFoundException {
        userMapper = MapperBuild.build(UserMapper.class);
    }

    @Override
    public List<User> findUsers(String name) {
        System.out.println("props is :"+name);
        List<User> res = new ArrayList<>();
//        res.add(new User("lu","1","123"));
//        res.add(new User("ze","1","123"));
//        res.add(new User("quan","1","123"));
        return res;
    }

    @Override
    public List<User> queryUserList(Map<String, Object> param) {
        return new ArrayList<>();
    }

    /**
     * 获取所有用户
     * @return
     */
    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    /**
     * 注册
     * @param user 用户对象
     * @return
     */
    @Override
    public boolean register(User user) {
        int i = userMapper.insertUser(user);
        return i > 0 ;
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }



    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
