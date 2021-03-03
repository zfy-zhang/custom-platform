package org.elisha.mybatis.framerwork.core.factory;

import org.elisha.mybatis.framerwork.core.sqlsession.SqlSession;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
