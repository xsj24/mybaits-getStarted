package com.github.xsj;

import com.github.xsj.model.Mapper;
import com.github.xsj.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: pc2
 * Date: 16-10-24
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class BaseTest {

    private final static Logger log =  LoggerFactory.getLogger(BaseTest.class);

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUp() throws Exception{
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
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
    public void selectTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Mapper mapper = session.getMapper(Mapper.class);
            assertThat(mapper.getUser(1), is(notNullValue()));
        } finally {
            session.close();
        }
    }

    @Test
    public void insertTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String expectedStr = "测试";
            Mapper mapper = session.getMapper(Mapper.class);
            User user = new User();
            user.setName(expectedStr);
            mapper.insertUser(user);
            session.commit();
            assertThat(mapper.getUser(2).getName(), is(equalTo(expectedStr)));
            log.info(String.valueOf(mapper.getUser(2)));
        } finally {
            session.close();
        }
    }

    @Test
    public void updateTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String expectedStr = "user_ttt";
            Mapper mapper = session.getMapper(Mapper.class);
            User user = mapper.getUser(1);
            user.setName(expectedStr);
            mapper.updateUser(user);
            session.commit();
            assertThat(mapper.getUser(1).getName(), is(equalTo(expectedStr)));
        } finally {
            session.close();
        }
    }

    @Test
    public void deleteTest() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Mapper mapper = session.getMapper(Mapper.class);
            User user = mapper.getUser(1);
            mapper.deleteUser(user.getId());
            session.commit();
            assertThat(mapper.getUser(1), is(nullValue()));
        } finally {
            session.close();
        }
    }

}
