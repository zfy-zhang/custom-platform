package org.elisha.web.mvc.deal.validate;


import java.lang.annotation.*;

/**
 * 校验分组
 * @author hukangning
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ValidationGroup {
	Class<?>[] value();
}
