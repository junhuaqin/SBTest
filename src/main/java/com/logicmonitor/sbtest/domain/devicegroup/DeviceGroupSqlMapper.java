package com.logicmonitor.sbtest.domain.devicegroup;

import com.logicmonitor.domain.store.AbstractSqlMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class DeviceGroupSqlMapper extends AbstractSqlMapper<DeviceGroup, DeviceGroupID> {
    private final static String _tblName = "device_groups";
    private final List<FieldTblMapper> _fieldTblMappers = new ArrayList<>();
    private final IDTblMapper _idTblMapper =
            new IDTblMapper("id",
                            ((rs, col) -> new DeviceGroupID(rs.getInt(col))),
                            (s, id, idx) -> s.setInt(idx, id.value()));

    public DeviceGroupSqlMapper() {
        _fieldTblMappers.add(
                new FieldTblMapper("name",
                        ResultSet::getString,
                        (s, o, idx) -> s.setString(idx, o.getName())));
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
    protected Class<DeviceGroup> getDomainObjectClass() {
        return DeviceGroup.class;
    }
}
