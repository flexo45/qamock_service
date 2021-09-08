package org.qamock.mock.scripts

import groovy.json.JsonSlurper

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns=
"urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/
ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><PostRegistrationRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn><FirstName>П***ич</FirstName>
<SecondName>П***ий</SecondName><Patronymic>К******</Patronymic><Birthday>***********</Birthday><PassportSeries>39**</PassportSeries>
<PassportNumber>202***</PassportNumber><Activities>3</Activities><Activities>4</Activities><Phone>79*******71</Phone>
<RequestTime>2021-08-24T09:04:00.788Z</RequestTime><Oktmo>12000000</Oktmo></PostRegistrationRequest></Message>
</SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 0).put("bind", 0)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def r = new XmlSlurper().parseText(req).Body.SendMessageRequest.Message.PostRegistrationRequest
def inn = r.Inn.text()
def activities = r.Activities.collect { it.text() }
def jobs = new JsonSlurper().parseText(utils.fileString("activities.json"))
def oktmo = r.Oktmo.text()
def regions = new JsonSlurper().parseText(utils.fileString("regions.json"))

def user = context.get(inn)

if (user.params.reg == 1) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "TAXPAYER_ALREADY_REGISTRED")
    params.put("ErrMessage", "Налогоплательщик уже поставлен на учет")
} else {
    def orderId = UUID.randomUUID().toString()
    context.get(inn)
            .put("activities", activities)
            .put("jobs", activities.collect { id ->
                ["id": id, "name": jobs.find { it.id.toString() == id }.name]
            })
            .put("oktmo", oktmo)
            .put("region", regions.find { it.oktmo == oktmo}.name)
            .put("regId", orderId)
            .put("x", 5)
    params.put("Message", response(orderId))
}

def response(orderId) {
    return """<PostRegistrationResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Id>$orderId</Id>
</PostRegistrationResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))
//println(context.get(inn).params)