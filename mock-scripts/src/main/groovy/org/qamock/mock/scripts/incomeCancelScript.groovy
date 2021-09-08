package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts-gnivc-ru/
inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
<Message><PostCancelReceiptRequestV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn>
<ReceiptId>rec**********3265</ReceiptId><ReasonCode>REGISTRATION_MISTAKE</ReasonCode></PostCancelReceiptRequestV2></Message></SendMessageRequest>
</soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 1)

//---START RESPONSE SCRIPT---//
def act = ["REFUND", "REGISTRATION_MISTAKE"]

def req = params.get("request")

def r = utils.xml(req).Body.SendMessageRequest.Message.PostCancelReceiptRequestV2
def inn = r.Inn.text()
def receipId = r.ReceiptId.text()
def reason = r.ReasonCode.text()

def user = context.get(inn)
List<Map> incomes = user.params.incomes
def inc = incomes == null ? null : incomes.find {it.recId == receipId }

if (user.params.reg == 0) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_unreg.code)
    params.put("ErrMessage", context.get("fns").params.err_unreg.text)
} else if (user.params.bind == 0) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", context.get("fns").params.err_unbound.code)
    params.put("ErrMessage", context.get("fns").params.err_unbound.text)
} else if (!act.contains(reason)) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "REQUEST_VALIDATION_ERROR")
    params.put("ErrMessage", "Неверный звпрос")
} else if (inc == null ) {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "RECEIPT_ID_NOT_FOUND")
    params.put("ErrMessage", "Не найден")
} else {
    inc.put("cancTime", new Date())
    params.put("Message", response())
}

def response() {
    return """<PostCancelReceiptResponseV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <RequestResult>DELETED</RequestResult>
</PostCancelReceiptResponseV2>"""
}
//---END RESPONSE SCRIPT---//

println(context.get("423050468026").params)
