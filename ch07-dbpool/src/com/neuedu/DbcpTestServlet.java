package com.neuedu;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.DataSourceConnectionFactory;

/**
 * Servlet implementation class DbcpTestServlet
 */
@WebServlet("/DbcpTestServlet")
public class DbcpTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method.equals("a")) {
			tomCatConfig(request, response);
		}else if(method.equals("b")) {
			propertiesConfig(request, response);
		}else if(method.equals("c")) {
			testDbcp(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	protected void testDbcp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//硬编码配置
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:TESTEDU");
			dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			dataSource.setUsername("scott");
			dataSource.setPassword("tiger");
			dataSource.setInitialSize(3);
			dataSource.setMaxTotal(10);
			dataSource.setMinIdle(3);
			dataSource.setMaxWaitMillis(5000);
			//从连接池中获取连接
			Connection conn = dataSource.getConnection();
			System.out.println(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	protected void propertiesConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//配置文件配置（推荐）
			Properties prop = new Properties();
			prop.load(getServletContext().getResourceAsStream("/WEB-INF/classes/dbcp_config.properties"));
			DataSource dataSource = BasicDataSourceFactory.createDataSource(prop);
			Connection conn = dataSource.getConnection();
			System.out.println(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	protected void tomCatConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Context initCtx = new InitialContext();
			//获取应用容器的配置信息envCtx-只能用在JavaEE环境
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//在envCtx配置中获取数据源
			DataSource dataSource = (DataSource)envCtx.lookup("jdbc/TestDataSource");
			Connection conn = dataSource.getConnection();
			System.out.println(conn);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
