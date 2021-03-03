package org.elisha.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Deprecated
public interface CustomController extends Controller {

    String findUsers(HttpServletRequest request, HttpServletResponse response) throws Throwable;

}
