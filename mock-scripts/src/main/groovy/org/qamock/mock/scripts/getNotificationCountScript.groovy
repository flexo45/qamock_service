package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest 
xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/
ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><GetNotificationsCountRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><inn>423050468026</inn></GetNotificationsCountRequest></Message></SendMessageRequest>
</soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("reg", 1)
        .put("bind", 1)
        .put("info", [phone: "345", mail: "sdf@vsd"])
        .put("notifs", [
                [id: 1, status: 0, title: "title 1", text: "text 1"],
                [id: 2, status: 0, title: "title 2", text: "text 2"]
        ])
        .put("oktmo", "2345342").put("activities", ["3", "2"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")
def inn = utils.xml(req).Body.SendMessageRequest.Message.GetNotificationsCountRequest.inn.text()

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
    params.put("Message", response(inn, user.params.notifs))
}

def response(inn, List notifs) {
    return """<GetNotificationsCountResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <status>
    <inn>$inn</inn>
    <count>${notifs == null ? 0 : notifs.findAll{ it.status == 0 }.size()}</count>
  </status>
</GetNotificationsCountResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))