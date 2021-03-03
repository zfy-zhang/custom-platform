package org.elisha.orm.base.load;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description: 对象加载器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ObjectLoader {

	private static final String FILE_PATH = "META-INF/objects/";


	private static final Logger logger = Logger.getLogger("base logger");

	private static  List<Object> objects = new ArrayList<>() ;

	private static  Map<String ,List<Object> > fileObjectsMap = new HashMap<>();


	private static Exception initException;

	static {
		try {
			URL root = Thread.currentThread().getContextClassLoader().getResource(FILE_PATH);
			File rootFile = new File(root.toURI());
			for (File file : rootFile.listFiles()){
				//不处理文件夹
				if (file.isDirectory()){
					continue;
				}
				List<String> classNames = IOUtils.readLines(new FileInputStream(file));
				List<Object> fileObjects = new ArrayList<>();
				for (String className : classNames){
					try {
						Object instance = Class.forName(className).newInstance();
						objects.add(instance);
						fileObjects.add(instance);
					}catch (Exception e){
						logger.log(Level.SEVERE , e.getMessage());
					}
				}
				fileObjectsMap.put(file.getName() , fileObjects);
			}
		} catch (Exception e) {
			initException = e;
		}
	}


	/**
	 * 获取所有对象
	 * @return 返回 {@link Object}的一个{@link List}
	 */
	public static List<Object> get(){
		return checkInit(() -> objects);
	}


	/**
	 * 根据文件名称获取对应文件下的对象
	 * @param fileName 文件名称
	 * @return 返回 {@link Object}的一个{@link List}
	 */
	public static List<Object> get(String fileName){
		return checkInit(() -> fileObjectsMap.get(fileName));
	}


	/**
	 * 校验初始化
	 * @param supplier 回调
	 * @param <I> 返回对象类型
	 * @return
	 */
	private static  <I>I checkInit(Supplier<I> supplier){
		if(initException != null){
			throw new IllegalStateException("对象加载器初始化失败" , initException);
		}
		return supplier.get();
	}

}
