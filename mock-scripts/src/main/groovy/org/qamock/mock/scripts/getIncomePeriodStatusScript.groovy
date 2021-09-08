package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts-
gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService
/types/1.0"><Message><GetIncomeForPeriodRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
<Inn>423050468026</Inn><TaxPeriodId>202105</TaxPeriodId></GetIncomeForPeriodRequest></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("reg", 1)
        .put("bind", 1)
        .put("202105", [total: 1234.32, cancel: 32.33, tax: 123.44])
        .put("info", [inn: "423050468026", phone: "345", mail: "sdf@vsd"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")
def r = utils.xml(req).Body.SendMessageRequest.Message.GetIncomeForPeriodRequest
def inn = r.Inn.text()
def period = r.TaxPeriodId.text()
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
    params.put("Message", response(user.params.get(period)))
}

def response(period) {
    if (period == null) period = [total: 0, cancel: 0, tax: 0]
    return """<GetIncomeForPeriodResponse xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersValidationService/types/1.0" xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
    <TotalAmount>$period.total</TotalAmount>
    <CanceledTotalAmount>$period.cancel</CanceledTotalAmount>
    <Tax>$period.tax</Tax>
</GetIncomeForPeriodResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))