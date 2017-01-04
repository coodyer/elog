package com.blog.web.base.dao;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.blog.web.entity.BeanFieldEntity;
import com.blog.web.entity.Record;
import com.blog.web.util.PropertUtil;
import com.blog.web.util.StringUtils;

@Service("jDBCUtil")
public class JDBCUtil {
	// 驱动程序名
	String driver = null;
	// URL指向要访问的数据库名scutcs
	String url = null;
	// MySQL配置时的用户名
	String user = null;
	// MySQL配置时的密码
	String password = null;

	public List<Record> querySql(String sql, Map<Integer, Object> paraMap) {
		ResultSet resultSet = null;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			// 打开连接对象
			conn = getConn();
			if (conn != null) {
				// statement用来执行SQL语句
				statement = conn.prepareStatement(sql);
				System.out.println("执行语句:" + sql);
				if (paraMap != null) {
					for (Integer key : paraMap.keySet()) {
						statement.setObject(key, paraMap.get(key));
					}
				}
				// 执行语句，返回结果
				resultSet = statement.executeQuery();
				return resultSetToList(resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接对象
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				conn.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public Integer querySqlUpdate(String sql, Map<Integer, Object> paraMap) {
		ResultSet resultSet = null;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			// 打开连接对象
			conn = getConn();
			if (conn != null) {
				// statement用来执行SQL语句
				statement = conn.prepareStatement(sql);
				System.out.println("执行语句:" + sql);
				if (paraMap != null) {
					for (Integer key : paraMap.keySet()) {
						statement.setObject(key, paraMap.get(key));
					}
				}
				Integer code = statement.executeUpdate();
				return code;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接对象
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				conn.close();
			} catch (Exception e2) {
			}
		}
		return 0;
	}

	public Boolean CallableUpdate(String callsql, Map<Integer, Object> paraMap)
			throws Exception {
		Connection conn = getConn();
		CallableStatement proc = null; // 创建执行存储过程的对象
		try {
			proc = conn.prepareCall(callsql); // 设置存储过程 call为关键字.
			if (paraMap != null) {
				for (Integer key : paraMap.keySet()) {
					proc.setObject(key, paraMap.get(key));
				}
			}
			proc.registerOutParameter(3, Types.INTEGER);
			proc.registerOutParameter(4, Types.VARCHAR);
			return proc.execute();// 执行
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (proc != null) {
					proc.close();
				}
				conn.close();
			} catch (Exception e2) {
			}
		}

	}

	public Map<Integer, Object> Callable(String callsql,
			Map<Integer, Object> paraMap, Map<Integer, Integer> outTypeMap)
			throws Exception {
		ResultSet resultSet = null;
		Connection conn = getConn();
		CallableStatement proc = null; // 创建执行存储过程的对象
		try {
			proc = conn.prepareCall(callsql); // 设置存储过程 call为关键字.
			if (paraMap != null) {
				for (Integer key : paraMap.keySet()) {
					proc.setObject(key, paraMap.get(key));
				}
			}
			if (outTypeMap != null) {
				for (Integer key : outTypeMap.keySet()) {
					proc.registerOutParameter(key, outTypeMap.get(key));
				}
			}
			resultSet = proc.executeQuery();// 执行

			Map<Integer, Object> allMap = new HashMap<Integer, Object>();
			if (outTypeMap != null) {
				for (Integer key : outTypeMap.keySet()) {
					allMap.put(key, proc.getObject(key));
				}
			}
			return allMap;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// 关闭连接对象
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (proc != null) {
					proc.close();
				}
				conn.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	private List<Record> resultSetToList(ResultSet resultSet) {
		String columnName = null;
		Object obj = null;
		List<Record> allRecord = new ArrayList<Record>();
		try {
			while (resultSet.next()) {
				// 枚举结果
				ResultSetMetaData data = resultSet.getMetaData();
				Record record = new Record();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					// 获得列名
					columnName = data.getColumnName(i);
					// 获得列值
					obj = resultSet.getObject(columnName);
					// 数据字段存入集合
					record.put(columnName, obj);
				}
				// 数据列存入集合
				allRecord.add(record);
			}
			return allRecord;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Record> findByField(Class cla, String fieldName,
			Object fieldValue) {
		if (StringUtils.findEmptyIndex(fieldName, fieldValue) > -1) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(fieldName, fieldValue);
		return findByFields(cla, map);
	}

	public List<Record> findByFields(Class cla, Map<String, Object> paraMap) {
		// 获取表名
		String tableName = PropertUtil.unParsParaName(getModelName(cla));
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ").append(tableName).append(" where 1=1 ");
		Map<Integer, Object> map = null;
		if (!StringUtils.isNullOrEmpty(paraMap)) {
			map = new HashMap<Integer, Object>();
			Object obj = null;
			for (String key : paraMap.keySet()) {
				obj = paraMap.get(key);
				if (StringUtils.isNullOrEmpty(obj)) {
					continue;
				}
				if (obj instanceof Collection<?> || obj instanceof Object[]) {
					List<Object> values = Arrays.<Object> asList(obj);
					sb.append(" and ")
							.append(key)
							.append("in (")
							.append(StringUtils.getByMosaicChr("?", ",",
									values.size())).append(") ");
					for (Object value : values) {
						map.put(map.size() + 1, value);
					}
					continue;
				}
				map.put(map.size() + 1, paraMap.get(key));
				sb.append(" and ").append(key).append("=? ");
			}
		}
		return querySql(sb.toString(), map);

	}

	public Object get(Class cla, Object priKeyValue) {
		List<Record> list = findByField(cla, "id", priKeyValue);
		if (list != null && !list.isEmpty()) {
			Record obj = list.get(0);
			return PropertUtil.recordToObject(cla, obj);
		}
		return null;
	}

	public Integer delete(Class cla, String priKeyName, Object priKeyValue) {
		if (StringUtils.findEmptyIndex(cla, priKeyName, priKeyValue) > -1) {
			return -1;
		}
		// 获取表名
		String tableName = PropertUtil.unParsParaName(getModelName(cla));
		StringBuilder sb = new StringBuilder();
		sb.append("delete ").append(tableName).append(" where ")
				.append(priKeyName).append("=? ");
		Map<Integer, Object> paraMap = new HashMap<Integer, Object>();
		paraMap.put(1, priKeyValue);
		return querySqlUpdate(sb.toString(), paraMap);
	}

	public Integer delete(Class cla, Object priKeyValue) {
		return delete(cla, "id", priKeyValue);
	}

	public Integer update(Object obj) {
		return update(obj, "id");
	}
	public String getPriKeyName(Object obj){
		
		return null;
	}
	public Integer update(Object obj, String priKeyName) {
		try {
			if (obj == null) {
				return -1;
			}
			// 获取表名
			String tableName = PropertUtil.unParsParaName(getModelName(obj));
			// 获取属性列表
			List<BeanFieldEntity> prpres = PropertUtil.getBeanFields(obj);
			if (prpres == null || prpres.isEmpty()) {
				return -1;
			}
			// 拼接SQL语句
			StringBuilder sql = new StringBuilder(MessageFormat.format(
					"update {0} set ", tableName));
			Map<Integer, Object> paraMap = new HashMap<Integer, Object>();
			BeanFieldEntity vo = null;
			String fieldName = null;
			Object priKeyValue = null;
			for (int i = 0; i < prpres.size(); i++) {
				vo = prpres.get(i);
				if (vo != null) {
					fieldName = PropertUtil.unParsParaName(vo.getFieldName());
					if (fieldName == null || "".equals(fieldName)) {
						continue;
					}
					if (fieldName.equals(priKeyName)) {
						priKeyValue = vo.getFieldValue();
						continue;
					}
					sql.append(fieldName).append("=?");
					// 封装参数
					paraMap.put(paraMap.size() + 1, vo.getFieldValue());
					if (i < prpres.size() - 1) {
						sql.append(",");
					}
				}
			}
			sql.append(" where ").append(priKeyName).append("=?");
			paraMap.put(paraMap.size() + 1, priKeyValue);
			return querySqlUpdate(sql.toString(), paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Integer insert(Object obj) {
		try {
			if (obj == null) {
				return -1;
			}
			// 获取表名
			String tableName = PropertUtil.unParsParaName(getModelName(obj));
			// 获取属性列表
			List<BeanFieldEntity> prpres = PropertUtil.getBeanFields(obj);
			if (prpres == null || prpres.isEmpty()) {
				return -1;
			}
			// 拼接SQL语句
			StringBuilder sql = new StringBuilder(MessageFormat.format(
					"insert into {0} set ", tableName));
			Map<Integer, Object> paraMap = new HashMap<Integer, Object>();
			BeanFieldEntity vo = null;
			String fieldName = null;
			for (int i = 0; i < prpres.size(); i++) {
				vo = prpres.get(i);
				if (vo != null) {
					fieldName = PropertUtil.unParsParaName(vo.getFieldName());
					if (fieldName == null || "".equals(fieldName)) {
						continue;
					}
					sql.append(fieldName).append("=?");
					// 封装参数
					paraMap.put(paraMap.size() + 1, vo.getFieldValue());
					if (i < prpres.size() - 1) {
						sql.append(",");
					}
				}
			}
			return querySqlUpdate(sql.toString(), paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	// 创建connection连接对象
	private Connection getConn() throws Exception {
		if (driver == null) {
			loadPropres();
		}
		// 加载驱动程序
		Class.forName(driver);
		// 连续数据库
		Connection conn = DriverManager.getConnection(url, user, password);
		conn.setAutoCommit(false);
		conn.setAutoCommit(true);
		return conn;
	}

	private void loadPropres() {
		Properties prop = null;
		InputStream inputStream = null;
		try {
			prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(
					"config/c3p0.properties");
			prop.load(inputStream);
			driver = prop.getProperty("jdbc.driverClass");
			url = prop.getProperty("jdbc.url");
			user = prop.getProperty("jdbc.user");
			password = prop.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				prop.clear();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			prop = null;
			inputStream = null;
		}
	}

	private static String getModelName(Object obj) {
		try {
			if(obj instanceof Class){
				return getModelName(obj);
			}
			return getModelName(obj.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getModelName(Class cla) {
		try {
			String classNmae = cla.getName();
			String packageName = cla.getPackage().getName();
			String modelName = classNmae.replace(packageName, "")
					.replace(".", "").replace("\\.", "");
			return modelName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
