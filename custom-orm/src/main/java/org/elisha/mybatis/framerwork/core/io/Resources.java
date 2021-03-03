package org.elisha.mybatis.framerwork.core.io;

import java.io.InputStream;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class Resources {

    /**
     * 文件资源加载类
     * @param location
     * @return
     */
    public static InputStream getResourceAsStream(String location) {
        return Resources.class.getClassLoader().getResourceAsStream(location);
    }
}
