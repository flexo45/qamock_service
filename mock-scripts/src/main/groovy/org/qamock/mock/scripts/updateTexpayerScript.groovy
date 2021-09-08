package org.qamock.mock.scripts

import groovy.json.JsonSlurper

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://
x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><Message><PutTaxpayerDataRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartners
IntegrationService/types/1.0"><Inn>423050468026</Inn><Activities>3</Activities><Activities>49</Activities><Activities>4</Activities>
<Region>45000000</Region></PutTaxpayerDataRequest></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("reg", 1)
        .put("bind", 1)
        .put("info", [phone: "345", mail: "sdf@vsd"])
        .put("oktmo", "2345342").put("activities", ["3", "2"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")
def r = utils.xml(req).Body.SendMessageRequest.Message.PutTaxpayerDataRequest
def inn = r.Inn.text()
def oktmo = r.Region.text()
def regions = new JsonSlurper().parseText(utils.fileString("regions.json"))
def activities = r.Activities
def jobs = new JsonSlurper().parseText(utils.fileString("activities.json"))
def phone = r.Phone.text()
def mail = r.Email.text()

user = context.get(inn)

if (user.params.reg == 0) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_unreg.code)
    params.put("ErrMessage", context.get("fns").params.err_unreg.text)
} else if (user.params.bind == 0) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_unbound.code)
    params.put("ErrMessage", context.get("fns").params.err_unbound.text)
} else {
    if (!oktmo.empty) {
        user.params.oktmo = oktmo
        user.put("region", regions.find { it.oktmo == oktmo}.name)
    }
    user.params.activities = activities.collect { it.text() }
    user.put("jobs", activities.collect { id ->
        [id: id.text(), name: jobs.find { it.id.toString() == id.text() }.name]
    })
    if (!phone.empty) { user.params.info.phone = phone }
    if (!mail.empty) { user.params.info.mail = mail }

    user.params.put("updateTime", new Date().format("yyyy-MM-dd'T'HH:mm:ss"))

    params.put("Message", response(user.params))
}

def response(user) {
    return """<PutTaxpayerDataResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <UpdateTime>$user.updateTime</UpdateTime>
</PutTaxpayerDataResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))