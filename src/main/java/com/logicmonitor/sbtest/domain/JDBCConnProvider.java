package com.logicmonitor.sbtest.domain;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class JDBCConnProvider{
    public static final HashMap<String, String> defaultParameters = new HashMap<>();
    protected static DataSource _ds;
    static {
        defaultParameters.put("MAXACTIVE", "150");
        defaultParameters.put("MAXIDLE", "50");
        defaultParameters.put("MAXWAIT", "180");
        defaultParameters.put("USERNAME", "root");
        defaultParameters.put("PASSWORD", "");
        defaultParameters.put("VALUDATIONQUERY", "/* ping */ SELECT 1");
        defaultParameters.put(" REMOVEABANDONED", "true");
        defaultParameters.put("LOGABANDONED", "true");
        defaultParameters.put("SERVER", "localhost:3306");
        defaultParameters.put("DRIVER", "com.mysql.jdbc.Driver");
        _ds = null;
    }

    public static void initConnectionPool(Map<String, String> customizedParas) throws Exception {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setMaxActive(Integer.parseInt(_getParameter("MAXACTIVE", customizedParas)));
        dataSource.setMaxIdle(Integer.parseInt(_getParameter("MAXIDLE", customizedParas)));
        dataSource.setMaxWait((long)Integer.parseInt(_getParameter("MAXWAIT", customizedParas)));
        dataSource.setUsername(_getParameter("USERNAME", customizedParas));
        dataSource.setPassword(_getParameter("PASSWORD", customizedParas));
        dataSource.setDriverClassName(_getParameter("DRIVER", customizedParas));
        dataSource.setValidationQuery(_getParameter("VALUDATIONQUERY", customizedParas));
        String url = _getParameter("URL", customizedParas);
        if (url == null) {
            dataSource.setUrl(String.format("jdbc:mysql://%s?dumpQueriesOnException=true&amp;useUnicode=true&amp;characterEncoding=UTF-8", _getParameter("SERVER", customizedParas)));
        } else {
            dataSource.setUrl(url);
        }

        dataSource.setRemoveAbandoned(Boolean.parseBoolean(_getParameter(" REMOVEABANDONED", customizedParas)));
        dataSource.setLogAbandoned(Boolean.parseBoolean(_getParameter("LOGABANDONED", customizedParas)));
        _ds = dataSource;
    }

    public Connection getConn() {
        try {
            Connection connection = _ds.getConnection();
            connection.setAutoCommit(false);
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String _getParameter(String key, Map<String, String> customizedParas) {
        String v = customizedParas.get(key);
        if (v == null) {
            v = defaultParameters.get(key);
        }

        return v;
    }
}
