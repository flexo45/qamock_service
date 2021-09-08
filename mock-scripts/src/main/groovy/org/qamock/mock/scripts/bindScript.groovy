package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns=
"urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/
ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><PostBindPartnerWithInnRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn><Permissions>INCOME_REGISTRATION</Permissions><Permissions>PAYMENT_INFORMATION
</Permissions><Permissions>TAX_PAYMENT</Permissions><Permissions>INCOME_LIST</Permissions><Permissions>INCOME_SUMMARY</Permissions>
<Permissions>CANCEL_ANY_INCOME</Permissions><Permissions>TAXPAYER_UPDATE</Permissions></PostBindPartnerWithInnRequest></Message>
</SendMessageRequest></soap:Body></soap:Envelope>"""

def err = """<SmzPlatformError xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Code>\${#params#ErrCode}</Code>
  <Message>\${#params#ErrMessage}</Message>
</SmzPlatformError>"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 1).put("bind", 0)
context.get("fns").put("error", err)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def inn = utils.xml(req).Body.SendMessageRequest.Message.PostBindPartnerWithInnRequest.Inn.text()
def perm = utils.xml(req).Body.SendMessageRequest.Message.PostBindPartnerWithInnRequest.Permissions

def user = context.get(inn)

if (user.params.bind == 1) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "TAXPAYER_ALREADY_BOUND")
    params.put("ErrMessage", "НП НПД уже привязан")
} else {
    def orderId = UUID.randomUUID().toString()
    user
            .put("bind", 2)
            .put("bindId", orderId)
            .put("perm", perm.collect { it.text() } )
    params.put("Message", response(orderId))
}

def response(orderId) {
    return """<PostBindPartnerWithInnResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Id>$orderId</Id>
</PostBindPartnerWithInnResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))