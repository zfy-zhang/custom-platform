package org.elisha.web.mvc.deal.validate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.elisha.orm.base.utiil.ReflectionUtils;
import org.elisha.web.mvc.proxy.Advice;
import org.elisha.web.mvc.proxy.MethodProcessor;
import org.hibernate.validator.HibernateValidator;

import javax.validation.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: meta information
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class BeanValidationAdvice implements Advice {


	/**
	 * 分组
	 */
	private Map<Method , Class<?>[]> groupMap = new HashMap<>();


	/**
	 * 目标class
	 */
	private Class clazz;

	/**
	 * 校验器
	 */
	private Validator validator;

	public BeanValidationAdvice(Class clazz){
		this.clazz = clazz;
		init();
	}


	/**
	 * 初始化
	 */
	private void init(){
		//初始化元数据分组
		List<Method> methods = ReflectionUtils.getMethods(clazz, Object.class);
		for (Method method : methods){
			ValidationGroup validationGroup = method.getAnnotation(ValidationGroup.class);
			if (validationGroup != null) {
				groupMap.put(method, validationGroup.value());
			}
		}
		//初始化校验器
		ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
				.configure().addProperty("fileEncodings" , "utf-8")
				.failFast( true ) //同.addProperty( "hibernate.validator.fail_fast", "true" )
				.buildValidatorFactory();
		this.validator = validatorFactory.getValidator();
	}




	@Override
	public Object invoke(MethodProcessor methodProcessor) throws Throwable {

		Method method = methodProcessor.getMethod();
		Class<?>[] groups = groupMap.get(method);
		Set<ConstraintViolation<Object>> constraintViolations;
		if (ArrayUtils.isEmpty(groups)){
			constraintViolations = validator.forExecutables().validateParameters(methodProcessor.getTargetObject(), method, methodProcessor.getArgs());
		}else {
			constraintViolations = validator.forExecutables().validateParameters(methodProcessor.getTargetObject() , method , methodProcessor.getArgs() , groups);
		}
		//校验结果如果为空则说明bean校验通过，则不需要处理
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			//错误信息
			StringBuffer msgSb = new StringBuffer();
			//遍历校验结果
			for (ConstraintViolation constraintViolation : constraintViolations) {
				msgSb.append("[");
				msgSb.append(constraintViolation.getPropertyPath());
				msgSb.append("]");
				msgSb.append(constraintViolation.getMessage());
				msgSb.append(",");
			}
			String msg = msgSb.toString();
			msg = StringUtils.substringBeforeLast(msg, ",");
			if (!StringUtils.isEmpty(msg)){
				throw new ValidationException(msg);
			}
		}
		return methodProcessor.invoke();
	}
}
