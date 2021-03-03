package org.elisha.custom.service;

import org.elisha.custom.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface UserService {
    List<User> findUsers(String name);

    List<User> queryUserList(Map<String, Object> param);

    /**
     * 注册用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    boolean register(User user);





    /**
     * 注销用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    boolean deregister(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return
     */
    boolean update(User user);



    List<User> queryAll();



    User queryUserById(Long id);




    User queryUserByNameAndPassword(String name, String password);
}
