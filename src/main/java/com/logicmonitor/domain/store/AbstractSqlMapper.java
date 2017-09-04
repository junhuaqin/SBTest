package com.logicmonitor.domain.store;

import com.logicmonitor.domain.Aggregate;
import com.logicmonitor.domain.id.ID;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public abstract class AbstractSqlMapper<T extends Aggregate<T, IT>, IT extends ID>
        implements SqlMapper<T, IT> {
    protected interface RS2Field<E> {
        E apply(ResultSet rs, String col) throws SQLException;
    }

    public interface BindField<E> {
        void apply(PreparedStatement statement, E obj, int index) throws SQLException;
    }

    public class FieldTblMapper {
        String name;
        RS2Field<?> rs2Field;
        BindField<T> binder;

        public FieldTblMapper(String name, RS2Field<?> rs2Field, BindField<T> binder) {
            this.name = name;
            this.rs2Field = rs2Field;
            this.binder = binder;
        }
    }

    public class IDTblMapper {
        String name;
        RS2Field<IT> rs2Field;
        BindField<IT> binder;

        public IDTblMapper(String name, RS2Field<IT> rs2Field, BindField<IT> binder) {
            this.name = name;
            this.rs2Field = rs2Field;
            this.binder = binder;
        }
    }

    protected abstract List<FieldTblMapper> getAggregateFields();
    protected abstract IDTblMapper getAggregateIDField();
    protected abstract String getTblName();
    protected abstract Class<T> getAggregateClass();

    protected String concatFields(Function<FieldTblMapper, String> mapper) {
        return getAggregateFields()
                .stream()
                .map(mapper::apply)
                .collect(Collectors.joining(","));
    }

    protected String listFields() {
        return concatFields(n -> n.name);
    }

    protected String createHoldersForFields() {
        return concatFields(n -> "?");
    }

    protected String createHoldersWithFieldsName() {
        return concatFields(n -> n.name + "=?");
    }

    @Override
    public StoreEntity<T, IT> rowAs(ResultSet rs) throws SQLException {
        List<Object> fields = new LinkedList<>();
        
        IDTblMapper idMapper = getAggregateIDField();
        IT id = idMapper.rs2Field.apply(rs, idMapper.name);
        fields.add(id);

        for (FieldTblMapper mapper : getAggregateFields()) {
            fields.add(mapper.rs2Field.apply(rs, mapper.name));
        }

        int n = fields.size();
        for (Constructor<?> c : getAggregateClass().getDeclaredConstructors()) {
            if (n == c.getParameterTypes().length) {
                try {
                    return new StoreEntity<>(id, ((Constructor<T>) c).newInstance(fields.toArray()));
                }
                catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RuntimeException(
                "constructor with number of parameters=" + n + "  not found in " + getAggregateClass());
    }

    @Override
    public String getQueryAllSql() {
        return String.format("SELECT %s,%s FROM %s",
                getAggregateIDField().name,
                listFields(),
                getTblName());
    }

    @Override
    public String getQueryOneSql() {
        return String.format("SELECT %s,%s FROM %s WHERE %s=?",
                getAggregateIDField().name,
                listFields(),
                getTblName(),
                getAggregateIDField().name);
    }

    @Override
    public void bindQueryOne(PreparedStatement statement, IT id) throws SQLException {
        getAggregateIDField().binder.apply(statement, id, 1);
    }

    @Override
    public String getAddSql() {
        return String.format("INSERT INTO %s(%s,%s) VALUES (?,%s)",
                getTblName(),
                getAggregateIDField().name,
                listFields(),
                createHoldersForFields());
    }

    @Override
    public void bindAdd(PreparedStatement statement, StoreEntity<T, IT> entity) throws SQLException {
        getAggregateIDField().binder.apply(statement, entity.getId(), 1);
        _bindAll(statement, entity.getAggregate(), 2);
    }

    @Override
    public String getDeleteSql() {
        return String.format("DELETE FROM %s WHERE %s=?",
                getTblName(),
                getAggregateIDField().name);
    }

    @Override
    public void bindDelete(PreparedStatement statement, IT id) throws SQLException {
        getAggregateIDField().binder.apply(statement, id, 1);
    }

    @Override
    public String getUpdateSql() {
        return String.format("UPDATE %s SET %s WHERE %s=?",
                getTblName(),
                createHoldersWithFieldsName(),
                getAggregateIDField().name);
    }

    @Override
    public void bindUpdate(PreparedStatement statement, StoreEntity<T, IT> entity) throws SQLException {
        int nextIndex = _bindAll(statement, entity.getAggregate(), 1);
        getAggregateIDField().binder.apply(statement, entity.getId(), nextIndex);
    }

    private int _bindAll(PreparedStatement statement, T object, int startIndex) throws SQLException {
        int index = startIndex;
        for (FieldTblMapper mapper : getAggregateFields()) {
                mapper.binder.apply(statement, object, index++);
        }

        return index;
    }
}
