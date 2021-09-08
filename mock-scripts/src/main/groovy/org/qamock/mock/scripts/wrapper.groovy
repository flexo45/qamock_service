package org.qamock.mock.scripts

import org.qamock.app.main.config.MockConfig
import org.qamock.app.main.dao.DynamicResourceDao
import org.qamock.app.main.dao.ScriptsDao
import org.qamock.app.main.domain.Content
import org.qamock.app.main.domain.DynamicRequestLog
import org.qamock.app.main.domain.DynamicResource
import org.qamock.app.main.domain.DynamicResourceMethod
import org.qamock.app.main.domain.DynamicResponse
import org.qamock.app.main.domain.Header
import org.qamock.app.main.domain.MockRequest
import org.qamock.app.main.domain.MockResponse
import org.qamock.app.main.domain.Script
import org.qamock.app.main.domain.ScriptSuite
import org.qamock.app.main.domain.Sequence
import org.qamock.app.main.dynamic.context.ContextUtilsImpl
import org.qamock.app.main.dynamic.context.TestContextServiceImpl
import org.qamock.app.main.dynamic.datagen.DataGeneratorImpl
import org.qamock.app.main.dynamic.script.ScriptUtilsImpl
import org.qamock.app.main.script.ScriptExecutorUtilImpl
import org.qamock.app.main.script.SequenceUtilImpl

class Wrapper {
    private static def instance = new Wrapper()
    private static def context = new TestContextServiceImpl()
    private static def params = [:]
    static def shared() {
        return instance
    }
    private def mock = new MockConfig()
    def getContext() {
        return new ContextUtilsImpl(context)
    }
    def getContext(Map<String, HashMap<String, Object>> initial) {
        def contextUtils = new ContextUtilsImpl(context)
        initial.each {
            contextUtils.get(it.key).params = it.value
        }
        return contextUtils
    }
    def getParams() {
        return params
    }
    def getGen() {
        return new DataGeneratorImpl()
    }
    def getUtils() {
        return new ScriptUtilsImpl(
                new ScriptExecutorUtilImpl(
                        mock.taskExecutor(),
                        new ScriptsDao() {
                            @Override
                            List<ScriptSuite> list() {
                                return null
                            }
                            @Override
                            ScriptSuite get(String name) {
                                return null
                            }
                            @Override
                            ScriptSuite get(long id) {
                                return null
                            }
                            @Override
                            void add(ScriptSuite scriptSuite) {

                            }
                            @Override
                            void update(ScriptSuite scriptSuite) {

                            }
                            @Override
                            void delete(ScriptSuite scriptSuite) {

                            }
                        },
                        mock.suiteProcessor()),
                new SequenceUtilImpl(new DynamicResourceDao() {

                    @Override
                    List<DynamicResource> getAllResourcesStartWith(String path) {
                        return null
                    }

                    @Override
                    DynamicResource getResource(String path) {
                        return null
                    }

                    @Override
                    DynamicResource getResource(long id) {
                        return null
                    }

                    @Override
                    DynamicResponse getResponse(long id) {
                        return null
                    }

                    @Override
                    MockRequest getMockRequest(long logId) {
                        return null
                    }

                    @Override
                    MockResponse getMockResponse(long logId) {
                        return null
                    }

                    @Override
                    Long addResource(DynamicResource resource) {
                        return null
                    }

                    @Override
                    Long addResponse(DynamicResponse response) {
                        return null
                    }

                    @Override
                    Long addContent(Content content) {
                        return null
                    }

                    @Override
                    Long addHeader(Header header) {
                        return null
                    }

                    @Override
                    Long addScript(Script script) {
                        return null
                    }

                    @Override
                    Long addResourceMethod(DynamicResourceMethod resourceMethod) {
                        return null
                    }

                    @Override
                    Long addDynamicRequestLog(DynamicRequestLog requestLog) {
                        return null
                    }

                    @Override
                    Long addMockRequest(MockRequest request) {
                        return null
                    }

                    @Override
                    Long addMockResponse(MockResponse response) {
                        return null
                    }

                    @Override
                    void updateResource(DynamicResource resource) {

                    }

                    @Override
                    void updateResponse(DynamicResponse response) {

                    }

                    @Override
                    void updateResponseHeader(Header header) {

                    }

                    @Override
                    void updateContent(Content content) {

                    }

                    @Override
                    void updateScript(Script script) {

                    }

                    @Override
                    void deleteResource(DynamicResource resource) {

                    }

                    @Override
                    void deleteResponse(DynamicResponse response) {

                    }

                    @Override
                    void deleteScript(Script script) {

                    }

                    @Override
                    void deleteResourceMethod(DynamicResourceMethod resourceMethod) {

                    }

                    @Override
                    void deleteResponseHeader(Header header) {

                    }

                    @Override
                    void deleteDynamicRequestLogsByResource(long resourceId) {

                    }

                    @Override
                    void deleteDynamicRequestLogsAll() {

                    }

                    @Override
                    List<DynamicResource> listResource() {
                        return null
                    }

                    @Override
                    List<DynamicRequestLog> listRequestLogs(int size) {
                        return null
                    }

                    @Override
                    List<DynamicRequestLog> listRequestLogsByResource(long resourceId, int size) {
                        return null
                    }

                    @Override
                    List<DynamicResourceMethod> listResourceMethods(long resourceId) {
                        return null
                    }

                    @Override
                    List<DynamicResponse> listDynamicResponses(long resourceId) {
                        return null
                    }

                    @Override
                    List<Header> listResponseHeaders(long responseId) {
                        return null
                    }

                    @Override
                    Content responseContent(long responseId) {
                        return null
                    }

                    @Override
                    Script responseScript(long responseId) {
                        return null
                    }

                    @Override
                    Script resourceScript(long resourceId) {
                        return null
                    }

                    @Override
                    Sequence getSequence(String name) {
                        return null
                    }

                    @Override
                    void addSequence(Sequence sequence) {

                    }

                    @Override
                    void updateSequence(Sequence sequence) {

                    }

                    @Override
                    void deleteSequence(Sequence sequence) {

                    }
                })
        )
    }
}