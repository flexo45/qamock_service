package org.qamock.service;

import org.qamock.dao.DynamicResourceDao;
import org.qamock.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    DynamicResourceDao resourceDao;

    @Override
    public void createSequence(String name, long startValue) {
        resourceDao.addSequence(new Sequence(name, startValue));
    }

    @Override
    public void deleteSequence(String name) {
        resourceDao.deleteSequence(resourceDao.getSequence(name));
    }

    @Transactional
    @Override
    public long nextSequenceNumber(String name) {
        Sequence sequence = resourceDao.getSequence(name);
        long next = sequence.getValue() + 1;
        sequence.setValue(next);
        resourceDao.updateSequence(sequence);
        return next;
    }

    @Transactional
    @Override
    public long currentSequenceNumber(String name) {
        return resourceDao.getSequence(name).getValue();
    }
}
