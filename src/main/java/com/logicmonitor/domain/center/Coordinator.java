package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.context.Context;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.store.StoreContext;
import com.logicmonitor.domain.store.StoreEntity;

import java.util.List;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class Coordinator implements Context{
    private final Context _repositoryCntx;
    private final StoreContext _storeCntx;

    public Coordinator(Context repositoryCntx, StoreContext storeCntx) {
        this._repositoryCntx = repositoryCntx;
        this._storeCntx = storeCntx;
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID> IT save(Class<T> clasz, CT createCommand) {
        IT id = _repositoryCntx.save(clasz, createCommand);
        _storeCntx.create(clasz).save(new StoreEntity<>(id, _repositoryCntx.get(clasz, id)));
        return id;
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID> T get(Class<T> clasz, IT id) {
        return _repositoryCntx.get(clasz, id);
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID> List<Event> process(Class<T> clasz, IT id, CT command) {
        return _repositoryCntx.process(clasz, id, command);
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID> void apply(Class<T> clasz, IT id, Event event) {
        _repositoryCntx.apply(clasz, id, event);
    }

    @Override
    public void commit() throws ContextException {
        try {
            _storeCntx.commit();
            _repositoryCntx.commit();
        }
        catch (Exception e) {
            throw new ContextException(e);
        }
    }

    @Override
    public void abort() {
        _storeCntx.abort();
        _repositoryCntx.abort();
    }
}
