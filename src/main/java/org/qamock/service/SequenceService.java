package org.qamock.service;

public interface SequenceService {

    void createSequence(String name, long startValue);

    void deleteSequence(String name);

    long nextSequenceNumber(String name);

    long currentSequenceNumber(String name);

}
