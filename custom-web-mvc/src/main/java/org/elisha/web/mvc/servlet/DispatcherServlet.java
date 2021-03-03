package org.elisha.web.mvc.servlet;

import org.elisha.web.mvc.context.WebApplicationContext;
import org.elisha.web.mvc.handler.CustomHandler;
import org.elisha.web.mvc.header.annotation.Controller;
import org.elisha.web.mvc.header.annotation.RequestMapping;
import org.elisha.web.mvc.header.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Description: mvn center
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class DispatcherServlet extends HttpServlet {


    /**
     * web mvc context
     */
    private WebApplicationContext webApplicationContext;
    // 存储url和对象的方法的映射关系。
    List<CustomHandler> handleList = new ArrayList<CustomHandler>();

    public void init() throws ServletException {
        // 初始化servlet时，读取初始化参数， classpath:springmvc.xml
        String contextConfigLocation = this.getServletConfig().getInitParameter("contextConfigLocation");
        // 创建SpringMvc容器
        webApplicationContext = new WebApplicationContext(contextConfigLocation);
        // 初始化SpringMvc容器
        webApplicationContext.refresh();
        // 初始化请求映射
        initHandleMapping();
        System.out.println("============" + handleList);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //进行请求的分发处理
        executeDisPatch(req,resp);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        this.doPost(req,resp);
    }

    // 初始化请求映射
    public void initHandleMapping(){
        // 判断IocMap对象中是否有bean对象
        if(webApplicationContext.iocMap.isEmpty()){
            return;
        }
        for(Map.Entry<String,Object> entry : webApplicationContext.iocMap.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            if(clazz.isAnnotationPresent(Controller.class)){
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for(Method declaredMethod : declaredMethods){
                    if(declaredMethod.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMappingAnnotation = declaredMethod.getAnnotation(RequestMapping.class);
                        // user/query
                        String url = requestMappingAnnotation.value();
                        handleList.add(new CustomHandler(url,entry.getValue(),declaredMethod));
                    }
                }
            }
        }
    }


    // 进行请求的分发处理
    public void executeDisPatch(HttpServletRequest req, HttpServletResponse resp){
        CustomHandler customHandler = getHandler(req);
        if(customHandler == null){
            try {
                resp.getWriter().println("<h1>handler is no found</h1>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Class<?>[] ParameterTypes = customHandler.getMethod().getParameterTypes();
            // 定义一个参数数组
            Object[] params = new Object[ParameterTypes.length];
            for(int i = 0; i < ParameterTypes.length; i ++){
                Class<?> parameterType = ParameterTypes[i];
                if("HttpServletRequest".equals(parameterType.getSimpleName())){
                    params[i] = req;
                }else if("HttpServletResponse".equals(parameterType.getSimpleName())){
                    params[i] = resp;
                }
            }

            // 获取请求中的集合
            Map<String,String[]> parameterMap = req.getParameterMap();
            for(Map.Entry<String,String[]> entry : parameterMap.entrySet()){
                String value = entry.getValue()[0];
                String name = entry.getKey();
                System.out.println("请求参数是：" + name + "========" + value);
                int index = hasRequestParam(customHandler.getMethod(),name);
                if(index != -1){
                    params[index] = value;
                }else {
                    List<String> names = getParameterNames(customHandler.getMethod());
                    System.out.println(names);
                    for(int i = 0; i < names.size(); i ++){
                        if(name.equals(names.get(i))){
                            params[i] = value;
                            break;
                        }
                    }
                }
            }

            // 调用控制中的方法
            try {
                Object result = customHandler.getMethod().invoke(customHandler.getController(),params);

                // 处理返回的结果
                if(result instanceof String){
                    // 跳转
                    String viewName = (String)result;
                    if(viewName.contains(":")){
                        System.out.println("viewName:" + viewName);
                        String viewType = viewName.split(":")[0];
                        String viewPage = viewName.split(":")[1];
                        if(viewType.equals("forward")){
                            req.getRequestDispatcher(viewPage).forward(req,resp);
                        }else{
                            resp.sendRedirect(viewPage);
                        }
                    }else {
                        if (!viewName.startsWith("/")) {
                            viewName = "/" + viewName;
                        }
                        // 默认的跳转
                        req.getRequestDispatcher(viewName).forward(req,resp);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 获取请求路径的handler
    public CustomHandler getHandler(HttpServletRequest req){
        // /user/query
        String requestURI = req.getRequestURI();
        for(CustomHandler myHandler : handleList){
            if(myHandler.getUrl().equals(requestURI)){
                return myHandler;
            }
        }
        return null;
    }

    // 判断控制器方法的参数，是否有RequestParam注解，且找到对应的value值，如果找到 返回这个参数的位置，如果没找到返回-1
    public int hasRequestParam(Method method, String name){
        Parameter[] parameters = method.getParameters();
        for(int i = 0; i < parameters.length; i ++){
            Parameter p = parameters[i];
            boolean b = p.isAnnotationPresent(RequestParam.class);
            if(b){
                RequestParam requestParam = p.getAnnotation(RequestParam.class);
                String requestParamValue = requestParam.value();
                if(name.equals(requestParamValue)){
                    return i;
                }
            }
        }
        return -1;
    }

    // 获取控制器方法的参数的名字
    public List<String> getParameterNames(Method method){
        List<String> list = new ArrayList<String>();
        Parameter[] parameters = method.getParameters();
        for(Parameter parameter : parameters){
            list.add(parameter.getName());
        }
        return list;
    }
}

