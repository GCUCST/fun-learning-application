package cn.cst.mybatis; // package cn.cst.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.Properties;

// MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class Plugins implements Interceptor {
    private Properties properties = new Properties();

    public Object intercept(Invocation invocation) throws Throwable {
        // implement pre processing if need
        System.out.println("进入....");
        Object proceed = invocation.proceed();
        ArrayList<Blog> blogs = (ArrayList) proceed;
        blogs.get(0).setName("改改改");
        System.out.println("end...." + blogs.size());
        // implement post processing if need
        return blogs;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
