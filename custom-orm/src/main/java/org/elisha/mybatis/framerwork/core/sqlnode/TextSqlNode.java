package org.elisha.mybatis.framerwork.core.sqlnode;

import org.elisha.mybatis.framerwork.core.utils.GenericTokenParser;
import org.elisha.mybatis.framerwork.core.utils.OgnlUtils;
import org.elisha.mybatis.framerwork.core.utils.SimpleTypeRegistry;
import org.elisha.mybatis.framerwork.core.utils.TokenHandler;

/**
 * @Description: 存储带有 ${} 的 SQL 文本信息
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/9
 * @Modify
 * @since
 */
public class TextSqlNode implements SqlNode{

    private String sqlText;

    public TextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    public boolean isDynamic() {
        return sqlText.indexOf("${") > -1;
    }

    @Override
    public void apply(DynamicContext context) {
        // 通用分词解析器
        // openToken：
        // closeToken
        // tokenHandler：被分出来的词该怎噩梦处理
        GenericTokenParser tokenParser = new GenericTokenParser("${", "}", new BindingTokenHandler(context));

        String sql = tokenParser.parse(sqlText);
        context.appendSql(sql);
    }

    private static class BindingTokenHandler implements TokenHandler {

        private DynamicContext context;

        public BindingTokenHandler(DynamicContext context) {
            this.context = context;
        }

        /**
         * 解析 ${}
         * @param content
         * @return 参数的值（sql字符串拼接需要）
         */
        @Override
        public String handleToken(String content) {
            Object parameter = context.getBindings().get("_parameter");
            if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
                return parameter.toString();
            }
            // 非简单类型的使用OGNL表达式去火丁指定参数的值
            Object value = OgnlUtils.getValue(content, parameter);
            return value == null ? "" : value.toString();
        }
    }
}
