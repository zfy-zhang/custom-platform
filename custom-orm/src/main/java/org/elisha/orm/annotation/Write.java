package org.elisha.orm.annotation;

import org.elisha.orm.matedata.MethodStatementMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Description: 写指令 更新，删除，新增
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SqlStatement(MethodStatementMetadata.StatementType.DLL_CUD)
public @interface Write {
	String value();
}
