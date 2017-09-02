package com.logicmonitor.domain;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface TransactionSupport {
    void commit() throws Exception;
    void abort();
}
