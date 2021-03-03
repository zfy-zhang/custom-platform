package org.elisha.orm;

import java.util.ArrayList;
import java.util.List;

/**
* 基于占位符的sql解析
 * ///TODO 目前只实现基本解析
* @author : KangNing Hu
*/
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class PlaceholderSqlResolve implements SqlResolve {

	private final static char LEFT_TOKE = '{';


	private final static char RIGHT_TOKEN = '}';


	private final static char PREPARED_TOKEN = '?';


	@Override
	public SqlWrapper doResolve(String sql) {
		char[] chars = sql.toCharArray();
		StringBuilder finalSql = new StringBuilder();
		List<String> paramNames = new ArrayList<>();
		StringBuffer paramName = new StringBuffer();
		boolean isParamName = false;
		for (char c : chars){
			 if (c == RIGHT_TOKEN){
				paramNames.add(paramName.toString());
				paramName = new StringBuffer();
				finalSql.append(" ");
				finalSql.append(PREPARED_TOKEN);
				finalSql.append(" ");
				isParamName = false;
			}else if (isParamName){
				paramName.append(c);
			}else if (c == LEFT_TOKE){
				isParamName = true;
			}else {
				finalSql.append(c);
			}
		}
		return new SqlWrapper(finalSql.toString() , paramNames);
	}


	public static void main(String[] args) {
		String sql = "select * from user where id = {id} and name = {name}";
		System.out.print(new PlaceholderSqlResolve().doResolve(sql));
	}
}
