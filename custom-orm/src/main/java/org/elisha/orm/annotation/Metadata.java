package org.elisha.orm.annotation;


import org.elisha.orm.matedata.MethodStatementMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 执行dml dcl
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SqlStatement(MethodStatementMetadata.StatementType.DML_AND_DCL)
public @interface Metadata {

	String value();
}
