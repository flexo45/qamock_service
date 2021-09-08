package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts
-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegra
tionService/types/1.0"><Message><GetIncomeReferenceRequestV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/ty
pes/1.0"><Inn>423050468026</Inn><RequestTime>2021-08-26T14:42:15.949Z</RequestTime><RequestYear>2021</RequestYear></GetIncomeReferenceRequestV2>
</Message></SendMessageRequest></soap:Body></soap:Envelope>"""

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
def message = utils.xml(req).Body.SendMessageRequest.Message
def inn, year, file

if (!message.GetIncomeReferenceRequestV2.toString().isEmpty()) {
    def r = message.GetIncomeReferenceRequestV2
    inn = r.Inn.text()
    year = r.RequestYear.text()
    file = utils.fileBase64("income-ref.pdf")
    params.put("Message", responseIncomeRef(file, year))
}
if (!message.GetRegistrationReferenceRequestV2.toString().isEmpty()) {
    def r = message.GetRegistrationReferenceRequestV2
    inn = r.Inn.text()
    year = r.RequestYear.text()
    file = utils.fileBase64("income-ref.pdf")
    params.put("Message", responseRegRef(file, year))
}

def responseRegRef(file, year) {
    return """<GetRegistrationReferenceResponseV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <RegistrationReferencePdf>
    <mimetype>application\\pdf</mimetype>
    <filename>Справка о постановке на учет за $year год.pd</filename>
    <content>$file</content>
  </RegistrationReferencePdf>
</GetRegistrationReferenceResponseV2>"""
}

def responseIncomeRef(file, year) {
    return """<GetIncomeReferenceResponseV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <IncomeReferencePdf>
    <mimetype>application\\pdf</mimetype>
    <filename>Справка о доходе за $year год.pd</filename>
    <content>$file</content>
  </IncomeReferencePdf>
</GetIncomeReferenceResponseV2>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))