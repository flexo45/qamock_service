package org.qamock.app.main.dao;

import org.qamock.app.main.domain.Connection;

public interface ConnectionDao {

    void add(Connection connection);

    Connection get(String name);

    Connection get(long id);

    void update(Connection connection);

    void delete(Connection connection);

}
