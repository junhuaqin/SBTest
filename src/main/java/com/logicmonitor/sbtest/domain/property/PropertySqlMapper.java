package com.logicmonitor.sbtest.domain.property;

import com.logicmonitor.domain.store.AbstractSqlMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class PropertySqlMapper extends AbstractSqlMapper<Property, PropertyID> {
    private final static String _tblName = "properties";
    private final List<FieldTblMapper> _fieldTblMappers = new ArrayList<>();
    private final IDTblMapper _idTblMapper =
            new IDTblMapper("id",
                            ((rs, col) -> new PropertyID(rs.getInt(col))),
                            (s, id, idx) -> s.setInt(idx, id.value()));

    public PropertySqlMapper() {
        _fieldTblMappers.add(
                new FieldTblMapper("key",
                        ResultSet::getString,
                        (s, o, idx) -> s.setString(idx, o.getKey())));

        _fieldTblMappers.add(
                new FieldTblMapper("value",
                        ResultSet::getString,
                        (s, o, idx) -> s.setString(idx, o.getValue())));
    }

    @Override
    protected List<FieldTblMapper> getDomainObjectFields() {
        return _fieldTblMappers;
    }

    @Override
    protected IDTblMapper getDomainObjectIDField() {
        return _idTblMapper;
    }

    @Override
    protected String getTblName() {
        return _tblName;
    }

    @Override
    protected Class<Property> getDomainObjectClass() {
        return Property.class;
    }
}