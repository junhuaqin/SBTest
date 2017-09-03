package com.logicmonitor.domain.view;

import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public interface View<IT extends ID> {
    IT getID();
}
