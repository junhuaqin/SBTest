package com.logicmonitor.domain.store;

import com.logicmonitor.domain.DomainObject;
import com.logicmonitor.domain.id.ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class ResultSetIterator<T extends DomainObject<T, IT>, IT extends ID>
        implements Iterator<StoreEntity<T, IT>> {
    private final Connection _connection;
    private final String _sql;
    private final SqlMapper<T, IT> _mapper;
    private ResultSet _rs;
    private PreparedStatement _ps;

    public ResultSetIterator(Connection connection, String sql, SqlMapper<T, IT> mapper) {
        this._connection = connection;
        this._sql = sql;
        this._mapper = mapper;
    }

    public void init() {
        try {
            _ps = _connection.prepareStatement(_sql);
            _rs = _ps.executeQuery();

        } catch (SQLException e) {
            close();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (_rs == null) {
            init();
        }
        try {
            boolean hasMore = _rs.next();
            if (!hasMore) {
                close();
            }
            return hasMore;
        } catch (SQLException e) {
            close();
            throw new RuntimeException(e);
        }

    }

    private void close() {
        try {
            _rs.close();
            try {
                _ps.close();
            } catch (SQLException e) {
                //nothing we can do here
            }
        } catch (SQLException e) {
            //nothing we can do here
        }
        finally {
            _rs = null;
            _ps = null;
        }
    }

    @Override
    public StoreEntity<T, IT> next() {
        try {
            return _mapper.rowAs(_rs);
        } catch (SQLException e) {
            close();
            throw new RuntimeException(e);
        }
    }
}
