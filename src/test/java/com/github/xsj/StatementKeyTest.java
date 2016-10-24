package com.github.xsj;

import com.github.xsj.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class StatementKeyTest {

    private final static Logger log =  LoggerFactory.getLogger(StatementKeyTest.class);

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUp() throws Exception{
        Reader reader = Resources.getResourceAsReader("mybatis-test-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        reader.close();
        // 数据初始化
        SqlSession session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();
        reader = Resources.getResourceAsReader("com/github/xsj/model/CreateDB.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(new PrintWriter(System.out));
        runner.runScript(reader);
        reader.close();
        conn.close();
        session.close();
    }

    @Test
    public void selectOneTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            User user = session.selectOne("com.github.xsj.model.Mapper.getUser", 1);
            assertThat(user, is(notNullValue()));
        } finally {
            session.close();
        }
    }

    @Test
    public void insertTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String expectedStr = "测试";
            User user = new User();
            user.setName(expectedStr);
            session.insert("com.github.xsj.model.Mapper.insertUser", user);
            session.commit();
            User otherUser = session.selectOne("com.github.xsj.model.Mapper.getUser", user.getId());
            assertThat(otherUser.getName(), is(equalTo(expectedStr)));
        } finally {
            session.close();
        }
    }

}
