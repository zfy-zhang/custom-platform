package org.elisha.custom.projects.user.web;


import org.elisha.orm.annotation.Metadata;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface DMLMapper {


	@Metadata("drop table  users")
	void dropTable();

	@Metadata("CREATE TABLE users(\n" +
			"id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n" +
			"name VARCHAR(16) NOT NULL,\n" +
			"password VARCHAR(64) NOT NULL,\n" +
			"email VARCHAR(64) NOT NULL,\n" +
			"phone_number VARCHAR(32) NOT NULL)")
	void createTable();
}
