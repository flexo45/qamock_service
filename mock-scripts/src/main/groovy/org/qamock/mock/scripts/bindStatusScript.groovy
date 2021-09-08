package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
<SendMessageRequest xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn:
//x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><GetBindPartnerStatusRequest xmlns="urn://x-artefact
s-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Id>6271b794-1fe3-4000-943b-75266c8a473d</Id></GetBindPartnerStatusRequest>
</Message></SendMessageRequest></soap:Body></soap:Envelope>"""

def err = """<SmzPlatformError xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Code>\${#params#ErrCode}</Code>
  <Message>\${#params#ErrMessage}</Message>
</SmzPlatformError>"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("info", [inn: "423050468026"])
        .put("reg", 1)
        .put("bind", 2)
        .put("perm", ["1", "2", "3"])
        .put("bindId", "6271b794-1fe3-4000-943b-75266c8a473d")
context.get("fns").put("error", err)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def orderId = utils.xml(req).Body.SendMessageRequest.Message.GetBindPartnerStatusRequest.Id.text()

def user = context.find(orderId).params

if (user == null) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_ise.code)
    params.put("ErrMessage", context.get("fns").params.err_ise.text)
} else if (user.bind == 1) {
    params.put("Message", response(user.info.inn, "COMPLETED", user.perm.collect { "<Permissions>$it</Permissions>" }.join(""), new Date().format("yyyy-MM-dd'T'HH:mm:ss")))
} else {
    params.put("Message", response(user.info.inn, "IN_PROGRESS", "", null))
}

def response(inn, status, permissions, time) {
    return """<GetBindPartnerStatusResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Result>$status</Result>
  <Inn>$inn</Inn>
  $permissions
  ${time == null ? "" : "<ProcessingTime>$time</ProcessingTime>"}
</GetBindPartnerStatusResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))