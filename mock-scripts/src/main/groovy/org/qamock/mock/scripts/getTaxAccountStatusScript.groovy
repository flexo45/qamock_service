package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts-gnivc-ru/
inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
<Message><GetTaxpayerAccountStatusRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn>
</GetTaxpayerAccountStatusRequest></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("reg", 1)
        .put("bind", 1)
        .put("account", [bonus: 9833.8773, unpaid: 32.33, debt: 123.44])
        .put("info", [inn: "423050468026", phone: "345", mail: "sdf@vsd"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")
def inn = utils.xml(req).Body.SendMessageRequest.Message.GetTaxpayerAccountStatusRequest.Inn.text()
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
    params.put("Message", response(user.params.account))
}

def response(account) {
    if (account == null) account = [bonus: 0, unpaid: 0, debt: 0]
    return """<GetTaxpayerAccountStatusResponse xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersValidationService/types/1.0" xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
    <BonusAmount>9833.8773</BonusAmount>
    <UnpaidAmount>0</UnpaidAmount>
    <DebtAmount>0</DebtAmount>
</GetTaxpayerAccountStatusResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))