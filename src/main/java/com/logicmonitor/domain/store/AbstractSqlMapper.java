package com.logicmonitor.domain.store;

import com.logicmonitor.domain.DomainObject;
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
public abstract class AbstractSqlMapper<T extends DomainObject<T, IT>, IT extends ID>
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

    protected abstract List<FieldTblMapper> getDomainObjectFields();
    protected abstract IDTblMapper getDomainObjectIDField();
    protected abstract String getTblName();
    protected abstract Class<T> getDomainObjectClass();

    protected String concatFields(Function<FieldTblMapper, String> mapper) {
        return getDomainObjectFields()
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
        IDTblMapper idMapper = getDomainObjectIDField();
        IT id = idMapper.rs2Field.apply(rs, idMapper.name);
        List<Object> fields = new LinkedList<>();
        for (FieldTblMapper mapper : getDomainObjectFields()) {
            fields.add(mapper.rs2Field.apply(rs, mapper.name));
        }

        int n = fields.size() + 1; // fields and id
        for (Constructor<?> c : getDomainObjectClass().getDeclaredConstructors()) {
            if (n == c.getParameterTypes().length) {
                try {
                    return new StoreEntity<>(id, ((Constructor<T>) c).newInstance(id, fields));
                }
                catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RuntimeException(
                "constructor with number of parameters=" + n + "  not found in " + getDomainObjectClass());
    }

    @Override
    public String getQueryAllSql() {
        return String.format("SELECT %s,%s FROM %s",
                getDomainObjectIDField().name,
                listFields(),
                getTblName());
    }

    @Override
    public String getQueryOneSql() {
        return String.format("SELECT %s,%s FROM %s WHERE %s=?",
                getDomainObjectIDField().name,
                listFields(),
                getTblName(),
                getDomainObjectIDField().name);
    }

    @Override
    public void bindQueryOne(PreparedStatement statement, IT id) throws SQLException {
        getDomainObjectIDField().binder.apply(statement, id, 1);
    }

    @Override
    public String getAddSql() {
        return String.format("INSERT INTO %s(%s,%s) VALUES (?,%s)",
                getTblName(),
                getDomainObjectIDField().name,
                listFields(),
                createHoldersForFields());
    }

    @Override
    public void bindAdd(PreparedStatement statement, StoreEntity<T, IT> entity) throws SQLException {
        getDomainObjectIDField().binder.apply(statement, entity.getId(), 1);
        _bindAll(statement, entity.getObject(), 2);
    }

    @Override
    public String getDeleteSql() {
        return String.format("DELETE FROM %s WHERE %s=?",
                getTblName(),
                getDomainObjectIDField().name);
    }

    @Override
    public void bindDelete(PreparedStatement statement, IT id) throws SQLException {
        getDomainObjectIDField().binder.apply(statement, id, 1);
    }

    @Override
    public String getUpdateSql() {
        return String.format("UPDATE %s SET %s WHERE %s=?",
                getTblName(),
                createHoldersWithFieldsName(),
                getDomainObjectIDField().name);
    }

    @Override
    public void bindUpdate(PreparedStatement statement, StoreEntity<T, IT> entity) throws SQLException {
        int nextIndex = _bindAll(statement, entity.getObject(), 1);
        getDomainObjectIDField().binder.apply(statement, entity.getId(), nextIndex);
    }

    private int _bindAll(PreparedStatement statement, T object, int startIndex) throws SQLException {
        int index = startIndex;
        for (FieldTblMapper mapper : getDomainObjectFields()) {
                mapper.binder.apply(statement, object, index++);
        }

        return index;
    }
}
