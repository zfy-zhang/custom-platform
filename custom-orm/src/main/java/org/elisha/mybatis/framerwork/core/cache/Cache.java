package org.elisha.mybatis.framerwork.core.cache;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public interface Cache {

    Object get(String key);

    void put(String key, Object value);
}
