package org.elisha.orm.base.id;

/**
 * @Description: id生成器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface IdGenerator {

	/**
	 * id生成方法
	 * @return
	 */
	Long generate();

	/**
	 * id生成方法
	 * @param dataId
	 * @return
	 */
	Long generate(int dataId);
}
