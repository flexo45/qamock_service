package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts-g
nivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationS
ervice/types/1.0"><Message><PostNotificationsDeliveredRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/typ
es/1.0"><notificationList><inn>423050468026</inn><messageId>20761</messageId></notificationList></PostNotificationsDeliveredRequest>
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
        .put("notifs", [
                [id: "1", status: 0, title: "title 1", text: "text 1"],
                [id: "2", status: 0, title: "title 2", text: "text 2"]
        ])
        .put("oktmo", "2345342").put("activities", ["3", "2"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")
def r = utils.xml(req).Body.SendMessageRequest.Message.PostNotificationsDeliveredRequest.notificationList
def inn = r.inn.text()
def messageId = r.messageId as List

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
    params.put("Message", response())
}

def response() {
    return """<PostNotificationsDeliveredResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <status>OK</status>
</PostNotificationsDeliveredResponse>"""
}
//---END RESPONSE SCRIPT---//
println(user.params.notifs)