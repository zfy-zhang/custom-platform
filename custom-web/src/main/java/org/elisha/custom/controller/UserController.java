package org.elisha.custom.controller;


import org.elisha.custom.domain.User;
import org.elisha.custom.service.UserService;
import org.elisha.web.mvc.controller.CustomController;
import org.elisha.web.mvc.controller.PageController;
import org.elisha.web.mvc.header.annotation.Autowired;
import org.elisha.web.mvc.header.annotation.Controller;
import org.elisha.web.mvc.header.annotation.RequestMapping;
import org.elisha.web.mvc.header.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/register")
    public String register(){
        return "register-form.jsp";
    }

    @RequestMapping("/user/doRegister")
    public String doRegister(HttpServletRequest request, HttpServletResponse resp){
        String email = request.getParameter("email");
        String password = request.getParameter("password");;

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        boolean register = userService.register(user);
        String view;
        if (register){
            //获取所以用户
            request.setAttribute("users" , userService.queryAll());
            view = "user/success.jsp";
        }else {
            view = "user/error.jsp";
        }
        return view;
    }

    @RequestMapping("/user/queryUser")
    @Deprecated
    public String queryUser(HttpServletRequest req, HttpServletResponse resp){
        Map map = new HashMap();
        map.put("username", "千年老亚瑟");
        map.put("sex", "男");
        List list = userService.queryUserList(map);
        System.out.println("user list" + list.toString());
        return "index.jsp";
    }

    @RequestMapping("/user/query")
    @Deprecated
    public void findUsers(HttpServletRequest req, HttpServletResponse resp,@RequestParam("name") String name){
        resp.setContentType("text/html;charset=utf-8");
        try{
            List<User> users = userService.findUsers(name);
            PrintWriter out = resp.getWriter();
            out.println("<h1>props is :"+name+"</h1>");
        }catch (Exception e){

        }
    }
}
