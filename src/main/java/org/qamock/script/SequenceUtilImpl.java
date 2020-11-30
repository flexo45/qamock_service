package org.qamock.script;

import org.qamock.dao.DynamicResourceDao;
import org.qamock.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SequenceUtilImpl {

    @Autowired
    DynamicResourceDao resourceDao;

    public void createSequence(String name, long startValue) {
        resourceDao.addSequence(new Sequence(name, startValue));
    }

    public void deleteSequence(String name) {
        resourceDao.deleteSequence(resourceDao.getSequence(name));
    }

    @Transactional
    public long nextSequenceNumber(String name) {
        Sequence sequence = resourceDao.getSequence(name);
        long next = sequence.getValue() + 1;
        sequence.setValue(next);
        resourceDao.updateSequence(sequence);
        return next;
    }

    @Transactional
    public long currentSequenceNumber(String name) {
        return resourceDao.getSequence(name).getValue();
    }
}
