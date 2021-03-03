package org.elisha.orm.base.utiil;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @Description: 反射工具类
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ReflectionUtils {


	private static  final Class[] GENERAL_TYPES = {String.class , Integer.class , int.class , short.class , Short.class , byte.class ,
			Byte.class , boolean.class , Boolean.class ,float.class , Float.class , Double.class , double.class , Long.class  , long.class ,Void.class , void.class };


	/**
	 * 获取被public修饰的方法
	 * @param clazz 目标对象class
	 * @param classes 需要过滤的classes
	 * @return 返回所有方法集合
	 */
	public static List<Method> getMethods(Class clazz , Class... classes){
		return getMember(clazz ,aClass -> Arrays.asList(aClass.getMethods())  , classes);
	}


	/**
	 * 获取所有的字段对象列表
	 * @param clazz 目标对象class
	 * @param classes 需要过滤的classes
	 * @return 返回所有字段对象
	 */
	public static  List<Field> getFields(Class clazz ,Class... classes ){
		return getMember(clazz , aClass -> Arrays.asList(aClass.getDeclaredFields()) , classes);
	}


	/**
	 * 是否是一般类型
	 * @param clazz
	 * @return
	 */
	public static  boolean isGeneralType(Class clazz){
		for (Class generalType : GENERAL_TYPES){
			if (clazz == generalType){
				return true;
			}
		}
		return false;
	}



	/**
	 * 是否为void类型
	 * @param clazz
	 * @return
	 */
	public static  boolean isVoid(Class clazz){
		return clazz == Void.class || clazz == void.class;
	}


	/**
	 * 调用field的get方法
	 * @param field
	 * @param target
	 * @return
	 */
	public static Object get(Field field , Object target){
		try {
			field.setAccessible(true);
			return field.get(target);
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 调用field的set方法
	 * @param field
	 * @param target
	 */
	public static void set(Field field , Object target , Object value){
		try {
			field.setAccessible(true);
			field.set(target ,value );
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
	}



	/**
	 *
	 * @param clazz 目标对象class
	 * @param classes 需要过滤的classes
	 * @return 返回所有方法集合
	 */
	public static <T> List<T> getMember(Class clazz , Function<Class , List<T>> function, Class... classes){
		List<Class> classList = ArrayUtils.isEmpty(classes) ? new ArrayList<>() : Arrays.asList(classes);
		List<T> list = new ArrayList<>();
		Class nextClass = clazz;
		do {
			list.addAll(function.apply(nextClass));
			Type genericSuperclass = nextClass.getGenericSuperclass();
			if (genericSuperclass instanceof Class && !classList.contains(genericSuperclass)){
				nextClass = (Class) clazz.getGenericSuperclass();
			}else {
				nextClass = null;
			}
		}while (nextClass != null);
		return list;
	}



	/**
	 * 获取注解
	 * @param tClass 目标对象 class
	 * @param <A> 注解类型
	 * @return 返回指定注解类型的对象
	 */
	public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement ,Class<A> tClass){
		List<A> annotations = getAnnotations(annotatedElement , tClass);
		return CollectionUtils.isEmpty(annotations) ? null : annotations.get(0);
	}


	/**
	 * 获取注解列表
	 * @param tClass 目标对象 class
	 * @param <A> 注解类型
	 * @return 返回指定类型的一个注解对象列表
	 */
	public static <A extends Annotation> List<A> getAnnotations(AnnotatedElement annotatedElement ,Class<A> tClass ){
		List<A> annotationList = new ArrayList<>();
		Annotation[] annotations = annotatedElement.getAnnotations();
		for (Annotation annotation : annotations){
			//直接标注
			if (annotation.annotationType().isAssignableFrom(tClass)){
				annotationList.add((A) annotation);
			}
			//间接标注
			final A annotation1 = annotation.annotationType().getAnnotation(tClass);
			if (annotation1 != null){
				annotationList.add(annotation1);
			}
		}
		return annotationList;
	}






	/**
	 * 校验是否标注clazz类型的注解
	 * @param annotatedElement 注解类型
	 * @return 如果标注了返回true反之亦然
	 */
	public static boolean  isAnnotation(AnnotatedElement annotatedElement , Class<? extends Annotation> clazz){
		Annotation[] annotations = annotatedElement.getAnnotations();
		for (Annotation annotation : annotations){
			if (annotation.annotationType().isAssignableFrom(clazz)){
				return true;
			}
			if (annotation.annotationType().getAnnotation(clazz) != null){
				return true;
			}
		}
		return false;
	}
}
