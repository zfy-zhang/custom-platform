package org.elisha.orm.base.utiil;


/**
 * @Description: 运行时工具类
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class RuntimeUtils {


	// ENV 运行环境 key
	private static final String SERVICE_ENV_PARAM_NAME = "SERVICE_ENV";
	private static final String MACHINE_CODE_PARAM_NAME =  "MACHINE_CODE";
	// ENV 运行环境
	private static final String ENV_PROD = "PROD";
	private static final String ENV_STAG = "STAGING";
	private static final String ENV_BETA = "BETA";
	private static final String ENV_DEV = "DEV";


	// 运行环境 value
	private static String ENV;
	private static String MACHINE_CODE;

	static {
		//获取环境类型
		String env = System.getenv(SERVICE_ENV_PARAM_NAME);
		if (ENV_STAG.equals(env)) {
			ENV = ENV_STAG;
		} else if (ENV_BETA.equals(env)) {
			ENV = ENV_BETA;
		} else if (ENV_PROD.equals(env)) {
			ENV = ENV_PROD;
		} else {
			ENV = ENV_DEV;
		}
		//获取机器码
		MACHINE_CODE = System.getenv(MACHINE_CODE_PARAM_NAME);
	}

	/**
	 * 获取当前jvm环境的机器码
	 *
	 * @return 机器码
	 */
	public static Long getMachineCode() {
		try {
			return Long.valueOf(MACHINE_CODE);
		}catch (Exception e){
			return 0L;
		}
	}

	/**
	 * 是否是正式环境
	 *
	 * @return 结果
	 */
	public static boolean isProdEnv() {
		return ENV.equals(ENV_PROD);
	}

	/**
	 * 是否是预发布环境
	 *
	 * @return 结果
	 */
	public static boolean isStagEnv() {
		return ENV.equals(ENV_STAG);
	}

	/**
	 * 是否是测试环境
	 *
	 * @return 结果
	 */
	public static boolean isBetaEnv() {
		return ENV.equals(ENV_BETA);
	}

	/**
	 * 是否是开发环境
	 *
	 * @return 结果
	 */
	public static boolean isDevEnv() {
		return ENV.equals(ENV_DEV);
	}



	/**
	 * 获取当前时间戳
	 * @return
	 */
	public static long getTimestamp(){
		return System.currentTimeMillis();
	}



}
