package org.elisha.custom.service.impl;


import org.elisha.custom.domain.User;
import org.elisha.custom.repository.UserMapper;
import org.elisha.custom.service.UserService;
import org.elisha.orm.MapperBuild;
import org.elisha.web.mvc.header.annotation.Autowired;
import org.elisha.web.mvc.header.annotation.Component;
import org.elisha.web.mvc.header.annotation.Service;
import org.hibernate.query.internal.NativeQueryImpl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Resource(name = "jpa/entityManager")
    private EntityManager entityManager;

    @PostConstruct
    public void init(){
        logger.log(Level.INFO , "初始化" + entityManager);
        System.out.println("初始化");
    }

    /**
     * 获取所有用户
     * @return
     */
    @Override
    public List<User> queryAll() {
        return entityManager
                .createNativeQuery("select  * from users" , User.class).unwrap(NativeQueryImpl.class)
                .getResultList();
    }

    @Override
    public List<User> findUsers(String name) {
        return null;
    }

    @Override
    public List<User> queryUserList(Map<String, Object> param) {
        return null;
    }

    /**
     * 注册
     * @param user 用户对象
     * @return
     */
    @Override
    public boolean register(@Valid User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.persist(user);
        return true ;

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
