package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn:
//x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3
/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><PostUnregistrationRequestV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ
/SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn><ReasonCode>RIGHTS_LOST</ReasonCode></PostUnregistrationRequestV2>
</Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 1).put("bind", 1)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def inn = utils.xml(req).Body.SendMessageRequest.Message.PostUnregistrationRequestV2.Inn.text()
def reason = utils.xml(req).Body.SendMessageRequest.Message.PostUnregistrationRequestV2.ReasonCode.text()
def user = context.get(inn)

if (user == null) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_ise.code)
    params.put("ErrMessage", context.get("fns").params.err_ise.text)
} else if (user.params.reg == 1) {
    def orderId = UUID.randomUUID().toString()
    user.put("unregId", orderId).put("unregReason", reason).put("x", 5)
    params.put("Message", response(orderId))
} else {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "TAXPAYER_ALREADY_UNREGISTRED")
    params.put("ErrMessage", "НП НПД уже снят с учета")
}
def response(orderId) {
    return """<PostUnregistrationResponseV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Id>$orderId</Id>
</PostUnregistrationResponseV2>"""
}
//---END RESPONSE SCRIPT---//