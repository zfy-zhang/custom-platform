package org.elisha.web.mvc.listener;

import org.elisha.orm.MapperBuild;
import org.elisha.web.mvc.context.ApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class SystemInitListener implements ServletContextListener {


	private static final Logger logger = Logger.getLogger("");




	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 获取数据源
		try {
			DataSource dataSource = ApplicationContext.getBean("jdbc/UserPlatformDB");
			// 初始化 orm
			MapperBuild.initDataSource(dataSource);
			logger.log(Level.INFO ,"orm初始化完成");
			try (final Connection connection = dataSource.getConnection();){
					logger.log(Level.INFO ,"JNDI加载数据源成功");
			}catch (Exception e){
				throw new IllegalStateException("JNDI加载数据源失败");
			}
			// 初始化数据表
			DMLMapper  dmlMapper = ApplicationContext.getBean("mapper/dmlMapper");
			try {
				dmlMapper.dropTable();
			}catch (Exception e){
				logger.log(Level.WARNING ,"数据表不存在");
			}
			try {
				dmlMapper.createTable();
			}catch (Exception e){
				logger.log(Level.WARNING ,"数据表已存在");
			}
			logger.log(Level.INFO ,"初始化数据表完成");
		}catch (Exception e){
			throw new IllegalStateException(e);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ApplicationContext.close();
	}



}
