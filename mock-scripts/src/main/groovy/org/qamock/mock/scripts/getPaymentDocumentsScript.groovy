package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts-gnivc-ru/i
nplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
<Message><GetPaymentDocumentsRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><InnList>423050468026</InnList>
</GetPaymentDocumentsRequest></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

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
def user, inn, file, response

if (!message.GetPaymentDocumentsRequest.toString().isEmpty()) {
    def r = message.GetPaymentDocumentsRequest
    inn = r.InnList.text()
    response =  responseDocuments(inn)
}

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
    params.put("Message", response)
}

def responseDocuments(inn) {
    return """<GetPaymentDocumentsResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <PaymentDocumentsList>
    <Inn>$inn</Inn>
    <DocumentList>
        <Type>DEBT</Type>
        <DocumentIndex>18209997210000011864</DocumentIndex>
        <FullName>Подготовленный Адепт Юрахипович</FullName>
        <Address>603093, РОССИЯ, , , г Нижний Новгород, , ул Родионова, 23А, , офис 307</Address>
        <Inn>216674662966</Inn>
        <Amount>3</Amount>
        <RecipientBankName>Отделение Брянск</RecipientBankName>
        <RecipientBankBik>041501001</RecipientBankBik>
        <RecipientBankAccountNumber>00000000000000000000</RecipientBankAccountNumber>
        <Recipient>Управление Федерального казначейства по Брянской области</Recipient>
        <RecipientAccountNumber>03100810300000010008</RecipientAccountNumber>
        <RecipientInn>7733535730</RecipientInn><RecipientKpp>773301001</RecipientKpp>
        <Kbk>18210506000011000110</Kbk>
        <Oktmo>15000000</Oktmo>
        <Code101>13</Code101>
        <Code106>ЗД</Code106>
        <Code107>31.08.2021</Code107>
        <Code110>0</Code110>
        <DueDate>2021-08-31</DueDate>
        <CreateTime>${new Date().format("yyyy-MM-dd'T'HH:mm:ss")}</CreateTime>
        <SourceId>70</SourceId>
    </DocumentList>
    <DocumentList>
        <Type>PENALTY</Type>
        <DocumentIndex>18209997210000011880</DocumentIndex>
        <FullName>Подготовленный Адепт Юрахипович</FullName>
        <Address>603093, РОССИЯ, , , г Нижний Новгород, , ул Родионова, 23А, , офис 307</Address>
        <Inn>216674662966</Inn>
        <Amount>0.78</Amount>
        <RecipientBankName>ОТДЕЛЕНИЕ МУРМАНСК</RecipientBankName>
        <RecipientBankBik>044705001</RecipientBankBik>
        <RecipientBankAccountNumber>00000000000000000000</RecipientBankAccountNumber>
        <Recipient>УФК по Мурманской области</Recipient>
        <RecipientAccountNumber>03100810040300017001</RecipientAccountNumber>
        <RecipientInn>7733535730</RecipientInn>
        <RecipientKpp>773301001</RecipientKpp>
        <Kbk>18210506000012100110</Kbk>
        <Oktmo>47000000</Oktmo>
        <Code101>13</Code101>
        <Code106>ЗД</Code106>
        <Code107>31.08.2021</Code107>
        <Code110>0</Code110>
        <DueDate>2021-08-31</DueDate>
        <CreateTime>${new Date().format("yyyy-MM-dd'T'HH:mm:ss")}</CreateTime>
        <SourceId>71</SourceId>
    </DocumentList>
</PaymentDocumentsList>
</GetPaymentDocumentsResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))