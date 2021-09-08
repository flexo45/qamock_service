package org.qamock.mock.scripts

import org.qamock.app.main.dynamic.context.TestContext

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
<SendMessageRequest xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn:
//x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message>
<GetUnregistrationStatusRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Id>6271b794-1fe3-4000-943b-75266c8a473d</Id>
</GetUnregistrationStatusRequest>
</Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("info", [inn: "423050468026"])
        .put("reg", 1)
        .put("bind", 1)
        .put("unregId", "6271b794-1fe3-4000-943b-75266c8a473d")
        .put("perm", ["1", "2", "3"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def orderId = utils.xml(req).Body.SendMessageRequest.Message.GetUnregistrationStatusRequest.Id.text()

def user = context.find(orderId).params

if (user.x > 0) {
    user.x -= 1
    params.put("Message", response("IN_PROGRESS", null))
} else if(user.x == 0) {
    user.reg = 0
    user.bind = 0
    user.x -= 1
    user.put("unregTime", new Date().format("yyyy-MM-dd'T'HH:mm:ss"))
    params.put("Message", response("COMPLETED", user.unregTime))
} else if (user.x > -10) {
    user.x -= 1
    params.put("Message", response("COMPLETED", user.unregTime))
} else {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "TAXPAYER_ALREADY_UNREGISTRED")
    params.put("ErrMessage", "НП НПД уже снят с учета")
}

def response(result, date) {
    return """<GetUnregistrationStatusResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <RequestResult>$result</RequestResult>
  ${date == null ? "" : "<UnregistrationTime>$date</UnregistrationTime>"}
</GetUnregistrationStatusResponse>"""
}
//---END RESPONSE SCRIPT---//
//<RejectionReason>$reason</RejectionReason>
println(FnsMock.getFnsResponse(params.Message))