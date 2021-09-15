//package ru.vtb.inn
//
//
//import groovyx.net.http.ContentType
//import groovyx.net.http.HTTPBuilder
//import groovyx.net.http.Method
//
//import java.text.SimpleDateFormat
//import java.time.LocalDateTime
//import java.time.ZoneOffset
//
//class GetClientChecker {
//    GetClientChecker(sbpUrl, ignoreDulCheck, ignores, misses, CheckerModel model) {
//        this.ignoreDulCheck = ignoreDulCheck
//        this.ignores = ignores
//        this.misses = misses
//        this.model = model
//        this.sbpUrl = sbpUrl
//    }
//    GetClientChecker(sbpUrl, CheckerModel model) {
//        this.ignoreDulCheck = false
//        this.ignores = false
//        this.misses = []
//        this.model = model
//        this.sbpUrl = sbpUrl
//    }
//    def sbpUrl
//
//    def ignoreDulCheck
//    def ignores
//    def misses
//
//    CheckerModel model
//
//    private Checker createChecker() {
//        new Checker(sbpUrl, ignoreDulCheck, ignores, misses, model)
//    }
//
//    def getAllClients(List<String> inns) {
//        new Thread(new GetAllClients(createChecker(), inns)).start()
//    }
//    def getClient(String inn) {
//        new Thread(new GetClient(createChecker(), inn)).start()
//    }
//    def getDul(String inn, String clientId) {
//        new Thread(new GetDul(createChecker(), inn, clientId)).start()
//    }
//    class GetAllClients implements Runnable {
//        GetAllClients(Checker checker, List<String> inns) {
//            this.checker = checker
//            this.inns = inns
//        }
//        private Checker checker
//        private List<String> inns
//        @Override
//        void run() {
//            inns.each {
//                checker.getClientByInn(it)
//            }
//        }
//    }
//    class GetClient implements Runnable {
//        GetClient(Checker checker, String inn) {
//            this.checker = checker
//            this.inn = inn
//        }
//        private Checker checker
//        private String inn
//        @Override
//        void run() {
//            checker.getClientByInn(inn)
//        }
//    }
//    class GetDul implements Runnable {
//        GetDul(Checker checker, String inn, String clientId) {
//            this.checker = checker
//            this.clientId = clientId
//            this.inn = inn
//        }
//        private Checker checker
//        private String clientId
//        private String inn
//        @Override
//        void run() {
//            checker.getDulByClientId(inn, clientId)
//        }
//    }
//}
//
//class Logger<T> {
//    Logger(T type, CheckerModel model) {
//        this.model = model
//        this.loggerKlass = type.class.simpleName
//    }
//    private String loggerKlass
//    private CheckerModel model
//    def log(String text) {
//        model.onLog("${loggerKlass}:$text")
//    }
//}
//
//class Checker {
//    // console
//    // ignore=zip,account
//    // miss=zip
//    static def main(args) {
//        def sbpUrl = args.find {it.contains("http")} ?: "http://localhost:8083/"
//        def model = new CheckerModel(sbpUrl)
//        if (args.contains("console")) {
//            def result = []
//            def ignoreDulCheck = args.any { it == "no-dul" }
//            def ignores = args.any { it.startsWith("ignore=") } ?
//                    args.find { it.startsWith("ignore=") }.split("=")[1].split(",") : []
//            def misses = args.any { it.startsWith("miss=") } ?
//                    args.find {it.startsWith("miss=") }.split("=")[1].split(",") : []
//            def checker = new Checker(sbpUrl, ignoreDulCheck, ignores, misses, model)
//            new File("inn.txt").readLines().each {
//                if (checker.checkSbpClient(it)) { result.add(it) }
//            }
//            println("Valid INN Found:")
//            result.each { println(it) }
//        } else {
//            def view = new CheckerView(model)
//            model.view = view
//            view.build()
//        }
//    }
//
//    Checker(sbpUrl, ignoreDulCheck, ignores, misses, CheckerModel model) {
//        this.httpSbp = new HTTPBuilder( sbpUrl )
//        httpSbp.parser['text/json'] = httpSbp.parser.'application/json'
//        this.isCheckDul = ignoreDulCheck == false
//        this.ignores = ignores
//        this.misses = misses
//        this.model = model
//    }
//    def httpSbp
//
//    def isCheckDul
//    def ignores
//    def misses
//    CheckerModel model
//    def clientValid = false
//    def dulValid = false
//
//    def getClientByInn(String inn) {
//        httpSbp.request(Method.POST) {
//            uri.path = 'bqr_reg/clients/get'
//            body = [inn: inn]
//            requestContentType = ContentType.JSON
//
//            response.success = { resp, json ->
//                json.Client.each {
//                    model.onClientFound(inn, it)
//                }
//            }
//
//            response.failure = { resp ->
//                println "Request failed with status ${resp.status}"
//            }
//        }
//    }
//
//    def getDulByClientId(String inn, String clientId) {
//        httpSbp.request(Method.POST) {
//            uri.path = 'bqr_reg/dul/check'
//            body = [id: clientId, requestId: UUID.randomUUID().toString()]
//            requestContentType = ContentType.JSON
//
//            response.success = { resp, json ->
//                model.onDulCheck(inn, clientId, json.DULValid == 1)
//            }
//
//            response.failure = { resp ->
//                println "Request failed with status ${resp.status}"
//            }
//        }
//    }
//
//    private def checkClient(inn, json) {
//        def clientId = json.id
//        println "Found client id=${clientId} name=${json.name}"
//        model.onClientFound(inn, json)
//        clientValid =
//                checkEq("dead", json.dead, 0) &&
//                        checkEq("doubtful", json.doubtful, 0) &&
//                        checkEq("blackList", json.blackList, 0) &&
//                        checkMoreThen("updateInfoTo",
//                                new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(json.updateInfoTo.toString()).time/1000,
//                                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) &&
//                        checkEq("has any phone", json.Phones.any { it.Phone != null }, true) &&
//                        checkEq("has sbp account", json.account.any { it.status_acc == 1 && it.status_sbp == 1 }, true) &&
//                        checkExist("city", json.city) &&
//                        checkExist("address", json.address) &&
//                        checkExist("ogrn", json.ogrn) &&
//                        checkExist("countryCode", json.countryCode) &&
//                        checkExist("countrySubDivisionCode", json.countrySubDivisionCode) &&
//                        checkExist("zip", json.zip) &&
//                        checkExist("name", json.name)
//
//
//    }
//
//    def checkSbpClient(inn) {
//        println "Start INN=${inn}"
//        httpSbp.request(Method.POST) {
//            uri.path = 'bqr_reg/clients/get'
//            body = [inn: inn]
//            requestContentType = ContentType.JSON
//
//            response.success = { resp, json ->
//                json.Client.each {
//                    checkClient(inn, it)
//                    checkDul(it.id)
//                    if (clientValid && dulValid) return
//                }
//            }
//
//            response.failure = { resp ->
//                println "Request failed with status ${resp.status}"
//            }
//        }
//        def isValid = clientValid && dulValid
//        println "--${isValid ? '> OK' : '-'}"
//        return isValid
//    }
//
//    private def checkDul(clientId) {
//        if (isCheckDul == false) {
//            dulValid = true
//            return
//        }
//
//        httpSbp.request(Method.POST) {
//            uri.path = 'bqr_reg/dul/check'
//            body = [id: clientId, requestId: UUID.randomUUID().toString()]
//            requestContentType = ContentType.JSON
//
//            response.success = { resp, json ->
//                dulValid = checkEq("DUL Valid", json.DULValid, 1)
//            }
//
//            response.failure = { resp ->
//                println "Request failed with status ${resp.status}"
//            }
//        }
//    }
//
//    private def checkEq(name, actual, expected) {
//        if(this.ignores.any { name.contains(it) }) return true
//
//        if (actual == expected) return true
//        println "INVALID ${name} -> ${actual}"
//        return false
//    }
//
//    private def checkMoreThen(name, actual, expected) {
//        if(this.ignores.any { it == name }) return true
//
//        if (actual > expected) return true
//        println "INVALID ${ name } -> ${ new Date(actual*1000 as long).toString() }"
//        return false
//    }
//
//    private def checkExist(name, actual) {
//        if(this.ignores.any { it == name }) return true
//
//        if(this.misses.any { it == name }) {
//            if(actual == null || actual == "") return true
//            println "INVALID ${name} existed"
//            return false
//        } else {
//            if (actual != null && actual != "") return true
//            println "INVALID ${name} not existed"
//            return false
//        }
//    }
//}