package org.qamock.mock.scripts

def x = """"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 0).put("bind", 0)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def orderId = new XmlSlurper().parseText(req).Body.SendMessageRequest.Message.GetRegistrationStatusRequest.Id.text()

def user = context.find(orderId).params

if (user.x > 0) {
    user.x -= 1
    params.put("Message", response(user, "IN_PROGRESS", null))
} else if (user.x == 0) {
    user.reg = 1
    user.bind = 1
    context.get(user.info.inn)
            .put("regTime", new Date().format("yyyy-MM-dd'T'HH:mm:ss"))
            .put("perm", [
                    "INCOME_REGISTRATION", "PAYMENT_INFORMATION", "TAX_PAYMENT", "INCOME_LIST", "INCOME_SUMMARY",
                    "CANCEL_ANY_INCOME", "TAXPAYER_UPDATE"
            ])
    params.put("Message", response(user, "COMPLETED", null))
} else if (user.x > -10) {
    user.x -= 1
    params.put("Message", response(user, "COMPLETED", null))
} else {
    params.put("Message", context.get("fns").params.error)
    params.put("ErrCode", "TAXPAYER_ALREADY_REGISTRED")
    params.put("ErrMessage", "Налогоплательщик уже поставлен на учет")
}

def response(user, result, rejection) {
    return """<GetRegistrationStatusResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <RequestResult>$result</RequestResult>
  ${rejection == null ? "" : "<RejectionReason>$rejection</RejectionReason>"}
  ${user.regTime == null ? "" : "<RegistrationTime>$user.regTime</RegistrationTime>"}
  <RegistrationCertificateNumber>24252</RegistrationCertificateNumber>
  <Inn>$user.info.inn</Inn>
</GetRegistrationStatusResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))

//        <UnregistrationTime>null</UnregistrationTime>
//  <BindRequestId>null</BindRequestId>
//        <LastRegistrationTime>2016-01-03T21:57:32.06</LastRegistrationTime>
//<UpdateTime>2010-05-15T02:33:18.69</UpdateTime>