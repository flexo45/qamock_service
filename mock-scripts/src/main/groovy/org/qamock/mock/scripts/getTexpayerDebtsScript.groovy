package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn:
//x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><Message><GetAccrualsAndDebtsRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><InnList>423050468026</InnList></GetAccrualsAndDebtsRequest></Message></SendMessageRequest>
</soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026")
        .put("reg", 1)
        .put("bind", 1)
        .put("debt", 333.34)
        .put("penny", 13.43)
        .put("tax", 1234.44)
        .put("info", [inn: "423050468026", phone: "345", mail: "sdf@vsd"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def inn = utils.xml(req).Body.SendMessageRequest.Message.GetAccrualsAndDebtsRequest.InnList.text()
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

def tax(user) {
    return """<TaxChargeList>
      <Amount>${user.tax}</Amount>
      <DueDate>${(new Date() + 30).format("yyyy-MM-dd'Z'")}</DueDate>
      <TaxPeriodId>${new Date().format("yyyyMM")}</TaxPeriodId>
      <Oktmo>${user.oktmo}</Oktmo>
      <Kbk>18210506000011000110</Kbk>
      <PaidAmount>0</PaidAmount>
      <CreateTime>${new Date().format("yyyy-MM-dd'T'HH:mm:ss")}</CreateTime>
      <Id>123</Id>
    </TaxChargeList>"""
}

def krbs(user) {
    return """<KrsbList>
      <Debt>${user.debt}</Debt>
      <Penalty>${user.penny}</Penalty>
      <Overpayment>0</Overpayment>
      <Oktmo>${user.oktmo}</Oktmo>
      <Kbk>18210506000011000110</Kbk>
      <UpdateTime>${new Date().format("yyyy-MM-dd'T'HH:mm:ss")}</UpdateTime>
      <Id>3327</Id>
    </KrsbList>"""
}

def response(user) {
    return """<GetAccrualsAndDebtsResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <AccrualsAndDebtsList>
    <Inn>${user.info.inn}</Inn>
    ${user.tax == null ? "" : tax(user)}
    ${user.tax == null ? "" : krbs(user)}
  </AccrualsAndDebtsList>
</GetAccrualsAndDebtsResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))