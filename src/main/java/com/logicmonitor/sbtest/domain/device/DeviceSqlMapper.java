package com.logicmonitor.sbtest.domain.device;

import com.logicmonitor.domain.store.AbstractSqlMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class DeviceSqlMapper extends AbstractSqlMapper<Device, DeviceID> {
    private final static String _TBL_NAME = "devices";
    private final List<FieldTblMapper> _fieldTblMappers = new ArrayList<>();
    private final IDTblMapper _idTblMapper =
            new IDTblMapper("id",
                            ((rs, col) -> new DeviceID(rs.getInt(col))),
                            (s, id, idx) -> s.setInt(idx, id.value()));
    private final String _tblName;

    public DeviceSqlMapper(final String dbName) {
        _tblName = String.format("%s.%s", dbName, _TBL_NAME);
        _fieldTblMappers.add(
                new FieldTblMapper("name",
                        ResultSet::getString,
                        (s, o, idx) -> s.setString(idx, o.getName())));
    }

    @Override
    protected List<FieldTblMapper> getAggregateFields() {
        return _fieldTblMappers;
    }

    @Override
    protected IDTblMapper getAggregateIDField() {
        return _idTblMapper;
    }

    @Override
    protected String getTblName() {
        return _tblName;
    }

    @Override
    protected Class<Device> getAggregateClass() {
        return Device.class;
    }
}
