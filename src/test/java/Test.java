import com.ly.swift.session.SwiftSqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Test {

    private Server server;


    private String port = "8082";
    private static String sourceURL1 = "jdbc:h2:mem:h2db";
    private static String sourceURL2 = "jdbc:h2:tcp://192.168.19.1:8082/mem:h2db";

    private String user = "sa";
    private String password = "";

    public void startServer() {
        try {
            System.out.println("正在启动h2...");
            server = Server.createTcpServer(
                    new String[]{"-tcpPort", port}).start();
        } catch (SQLException e) {
            System.out.println("启动h2出错：" + e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        if (server != null) {
            System.out.println("正在关闭h2...");
            server.stop();
            System.out.println("关闭成功.");
        }
    }

    public void testH2() {
        try {
            // 加载驱动
            Class.forName("org.h2.Driver");

            // 创建连接
            Connection conn = DriverManager.getConnection(sourceURL1, user, password);
            Statement stat = conn.createStatement();

            // 插入数据
            stat.execute("CREATE MEMORY Table table_one(NAME VARCHAR)");
            stat.execute("INSERT INTO table_one VALUES('this is my first program!')");

            // 查询数据
            ResultSet result = stat.executeQuery("select name from table_one ");
            int i = 1;
            while (result.next()) {
                System.out.println(i++ + ":" + result.getString("name"));
            }
            result.close();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        Test h2 = new Test();

        // 开始服务
        h2.startServer();
        h2.testH2();


        SqlSessionFactoryBuilder builder = new SwiftSqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(Resources.getResourceAsStream("conf.xml"));
        try (SqlSession sqlSession = factory.openSession()) {
            sqlSession.getMapper(List.class);
        }
        // 关闭服务
        h2.stopServer();
    }


}
