package com.logicmonitor.domain.store;

import com.logicmonitor.domain.Aggregate;
import com.logicmonitor.domain.id.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface SqlMapper<T extends Aggregate<T, IT>, IT extends ID> {
    StoreEntity<T, IT> rowAs(ResultSet rs) throws SQLException;

    String getQueryAllSql();
    String getQueryOneSql();
    void bindQueryOne(PreparedStatement statement, IT id) throws SQLException;

    String getAddSql();
    void bindAdd(PreparedStatement statement, StoreEntity<T, IT> entity) throws SQLException;

    String getDeleteSql();
    void bindDelete(PreparedStatement statement, IT id) throws SQLException;

    String getUpdateSql();
    void bindUpdate(PreparedStatement statement, StoreEntity<T, IT> entity) throws SQLException;
}
