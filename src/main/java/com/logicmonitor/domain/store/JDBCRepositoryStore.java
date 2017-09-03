package com.logicmonitor.domain.store;

import com.logicmonitor.domain.Aggregate;
import com.logicmonitor.domain.id.ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class JDBCRepositoryStore<T extends Aggregate<T, IT>, IT extends ID>
        implements RepositoryStore<T, IT> {
    private final SqlMapper<T, IT> _sqlMapper;
    private final Connection _conn;

    public JDBCRepositoryStore(final Connection conn, final SqlMapper<T, IT> sqlMapper) {
        _conn = conn;
        _sqlMapper = sqlMapper;
    }

    @Override
    public void save(StoreEntity<T, IT> entity) {
        _executeUpdateOnce(_sqlMapper.getAddSql(), entity, _sqlMapper::bindAdd);
    }

    @Override
    public void saveAll(Collection<StoreEntity<T, IT>> storeEntities) {
        _executeUpdateBatch(_sqlMapper.getAddSql(), storeEntities, _sqlMapper::bindAdd);
    }

    @Override
    public StoreEntity<T, IT> findOne(IT id) {
        try {
            PreparedStatement statement = _conn.prepareStatement(_sqlMapper.getQueryOneSql());
            _sqlMapper.bindQueryOne(statement, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return _sqlMapper.rowAs(rs);
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<StoreEntity<T, IT>> findAll(boolean parallel) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(
                        new ResultSetIterator<>(_conn, _sqlMapper.getQueryAllSql(), _sqlMapper),
                        Spliterator.IMMUTABLE),
                        parallel);
    }

    @Override
    public Stream<StoreEntity<T, IT>> findAll() {
        return findAll(false);
    }

    @Override
    public Stream<StoreEntity<T, IT>> findAllParallel() {
        return findAll(true);
    }

    @Override
    public void delete(IT id) {
        _executeUpdateOnce(_sqlMapper.getDeleteSql(), id, _sqlMapper::bindDelete);
    }

    @Override
    public void deleteAll(Collection<IT> ids) {
        _executeUpdateBatch(_sqlMapper.getDeleteSql(), ids, _sqlMapper::bindDelete);
    }

    @Override
    public void update(StoreEntity<T, IT> entity) {
        _executeUpdateOnce(_sqlMapper.getUpdateSql(), entity, _sqlMapper::bindUpdate);
    }

    private interface Binder<E> {
        void bind(PreparedStatement statement, E obj) throws SQLException;
    }

    private <E> void _executeUpdateOnce(String sql, E obj, Binder<E> binder) {
        System.out.println(sql);
//        try {
//            PreparedStatement statement = _conn.prepareStatement(sql);
//            binder.bind(statement, obj);
//            statement.executeUpdate();
//        }
//        catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    private <E> void _executeUpdateBatch(String sql, Collection<? extends E> objs, Binder<E> binder) {
        try {
            PreparedStatement statement = _conn.prepareStatement(sql);
            for (E obj : objs) {
                binder.bind(statement, obj);
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
