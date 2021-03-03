package org.elisha.web.mvc.context;

import org.elisha.web.mvc.header.annotation.Autowired;
import org.elisha.web.mvc.header.annotation.Controller;
import org.elisha.web.mvc.header.annotation.Mapper;
import org.elisha.web.mvc.header.annotation.Service;
import org.elisha.web.mvc.xml.XMLParse;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class WebApplicationContext {

    // classpath:springmvc.xml地址
    String contexConfigLocation;
    List<String> classNameList = new ArrayList<>();
    // springioc容器
    public Map<String,Object> iocMap = new ConcurrentHashMap<String,Object>();

    public WebApplicationContext(String contextConfigLocation) {
        this.contexConfigLocation = contextConfigLocation;
    }

    /**
     *初始化spring容器
     */
    public void refresh(){
        // 通过DOM4j解析 Springmvc.xml
        String basePackage = XMLParse.getBasePackage(contexConfigLocation.split(":")[1]);
        String[] basePackages = basePackage.split(",");
        System.out.println("扫描basePackage =====" + basePackage);
        if(basePackages.length > 0){
            for(String pack : basePackages){
                // 扫描org.elisha.custom.service
                // 扫描org.elisha.custom.controller
                executeScanPackage(pack);

            }
        }
        System.out.println("扫描内容是 =====" + classNameList);
        // 实例化bean
        executeInstance();
        System.out.println("spring容器对象是========="+iocMap);
        executeAutowired();
    }
    // 扫描包
    public void executeScanPackage(String pack){
        // 得到一个URL org.elisha.custom.controller
        URL url = this.getClass().getClassLoader().getResource("/"+pack.replaceAll("\\.","/"));
        String path = url.getFile();
        // 得到一个文件夹 /com/elisha/custom/service
        File dir = new File(path);
        for(File f : dir.listFiles()){
            if(f.isDirectory()){
                // 当前还是一个文件夹，递归 比如org.elisha.custom.service.impl
                executeScanPackage(pack+"."+f.getName());
            }else{
                // 当前是目录下的文件 获取文件的全路径
                String className = pack+"."+f.getName().replaceAll(".class","");
                classNameList.add(className);
            }
        }
    }

    // 实例化bean
    public void executeInstance(){
        // 判断有没有扫描到需要实例化的bean
        if(classNameList.size() == 0) return;
        // 如果有那么就通过反射创建对象放在map中
        try{
            for(String className : classNameList){
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(Controller.class)){
                    // 控制层的类org.elisha.custom.controller
                    // userController
                    String beanName = clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1);
                    iocMap.put(beanName,clazz.newInstance());
                }else if(clazz.isAnnotationPresent(Service.class)){
                    Service serviceAnnotation = clazz.getAnnotation(Service.class);
                    String beanName = serviceAnnotation.value();
                    if ("".equals(beanName)) {
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for(Class<?> c1 : interfaces){
                            String beanName1 = c1.getSimpleName().substring(0,1).toLowerCase()+c1.getSimpleName().substring(1);
                            iocMap.put(beanName1,clazz.newInstance());
                        }
                    }else {
                        iocMap.put(beanName,clazz.newInstance());
                    }
                } else if (clazz.isAnnotationPresent(Mapper.class)) {
                    Mapper mapperAnnotation = clazz.getAnnotation(Mapper.class);
                    String beanName = mapperAnnotation.value();
                    if ("".equals(beanName)) {
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for(Class<?> c1 : interfaces){
                            String beanName1 = c1.getSimpleName().substring(0,1).toLowerCase()+c1.getSimpleName().substring(1);
                            iocMap.put(beanName1,clazz.newInstance());
                        }
                    }else {
                        iocMap.put(beanName,clazz.newInstance());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // 实例化Spring中的依赖注入
    public void executeAutowired(){
        if(iocMap.isEmpty()){
            return;
        }
        for(Map.Entry<String,Object> entry : iocMap.entrySet()){
            String key = entry.getKey();
            Object bean = entry.getValue();
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for(Field declareField : declaredFields){
                if(declareField.isAnnotationPresent(Autowired.class)){
                    Autowired autowiredAnnotation = declareField.getAnnotation(Autowired.class);
                    String beanName = autowiredAnnotation.value();
                    if ("".equals(beanName)) {
                        Class<?> type = declareField.getType();
                        beanName = type.getSimpleName().substring(0,1).toLowerCase() + type.getSimpleName().substring(1);
                    }
                    declareField.setAccessible(true);
                    try {
                        declareField.set(bean,iocMap.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
