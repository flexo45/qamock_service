package org.qamock.app.main.service;

import org.qamock.app.main.domain.Connection;
import org.qamock.app.main.dao.ConnectionDao;
import org.qamock.app.main.dao.ScriptsDao;
import org.qamock.app.main.domain.ScriptSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private ConnectionDao connectionDao;

    @Autowired
    private ScriptsDao scriptsDao;

    @Transactional
    @Override
    public List<ScriptSuite> getSuiteList() {
        return scriptsDao.list();
    }

    @Transactional
    @Override
    public void createSuite(ScriptSuite scriptSuite) {
        scriptsDao.add(scriptSuite);
    }

    @Transactional
    @Override
    public void updateSuite(ScriptSuite scriptSuite) {
        scriptsDao.update(scriptSuite);
    }

    @Transactional
    @Override
    public void deleteSuite(ScriptSuite scriptSuite) {
        scriptsDao.delete(scriptSuite);
    }

    @Transactional
    @Override
    public ScriptSuite getSuite(long id) {
        return scriptsDao.get(id);
    }

    @Transactional
    @Override
    public Connection getConnection(String name){
        return connectionDao.get(name);
    }

    @Transactional
    @Override
    public Connection getConnection(long id){
        return connectionDao.get(id);
    }

    @Override
    @Deprecated
    public void reloadScript(String name) {

    }

    @Override
    @Deprecated
    public void reloadScripts() {

    }

}
