package org.qamock.dao;

import org.qamock.domain.Connection;

public interface ConnectionDao {

    void add(Connection connection);

    Connection get(String name);

    Connection get(long id);

    void update(Connection connection);

    void delete(Connection connection);

}
