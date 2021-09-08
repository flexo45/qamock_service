package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts
-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegration
Service/types/1.0"><Message><GetTaxpayerStatusRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
<Inn>423050468026</Inn></GetTaxpayerStatusRequest></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 1).put("bind", 1)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def inn = utils.xml(req).Body.SendMessageRequest.Message.GetTaxpayerStatusRequest.Inn.text()
def user = context.get(inn)

if (user.params.reg == 0) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_unreg.code)
    params.put("ErrMessage", context.get("fns").params.err_unreg.text)
} else if (user.params.bind == 0) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_unbound.code)
    params.put("ErrMessage", context.get("fns").params.err_unbound.text)
} else {
    params.put("Message", response(user.params))
}

def response(user) {
    return """<GetTaxpayerStatusResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <FirstName>${user.info.first}</FirstName>
  <SecondName>${user.info.last}</SecondName>
  <Patronymic>${user.info.mid}</Patronymic>
  ${user.regTime == null ? "" : "<RegistrationTime>$user.regTime</RegistrationTime>"}
  ${user.unregTime == null ? "" : "<UnregistrationTime>$user.unregTime</UnregistrationTime>"}
  ${user.unregReason == null ? "" : "<UnregistrationReason>$user.unregReason</UnregistrationReason>"}
  ${user.activities.collect{ "<Activities>$it</Activities>" }}
  <Region>${user.oktmo}</Region>
  <Phone>${user.info.phone}</Phone>
  <Email>${user.info.mail}</Email>
  <AccountNumber>string</AccountNumber>
  ${user.updateTime == null ? "" : "<UpdateTime>$user.updateTime</UpdateTime>"}
  <RegistrationCertificateNumber>string</RegistrationCertificateNumber>
</GetTaxpayerStatusResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))