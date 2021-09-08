package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="
urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/
SmzPartnersIntegrationService/types/1.0"><Message><GetNotificationsRequest xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegration
Service/types/1.0"><notificationsRequest><inn>423050468026</inn><GetAcknowleged>true</GetAcknowleged><GetArchived>false</GetArchived>
</notificationsRequest></GetNotificationsRequest></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

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
                [id: 1, status: 0, title: "title 1", text: "text 1", date: (new Date() - 1).format("yyyy-MM-dd'T'HH:mm:ss")],
                [id: 2, status: 1, title: "title 2", text: "text 2", date: (new Date() - 2).format("yyyy-MM-dd'T'HH:mm:ss")],
                [id: 2, status: 2, title: "title 2", text: "text 2", date: (new Date() - 4).format("yyyy-MM-dd'T'HH:mm:ss")]
        ])
        .put("oktmo", "2345342").put("activities", ["3", "2"])

//---START RESPONSE SCRIPT---//
def req = params.get("request")
def r = utils.xml(req).Body.SendMessageRequest.Message.GetNotificationsRequest.notificationsRequest
def inn = r.inn.text()
ack = Boolean.parseBoolean(r.GetAcknowleged.text())
arch = Boolean.parseBoolean(r.GetArchived.text())

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
    params.put("Message", response(inn, user.params.notifs))
}
def status(n) {
    switch (n) {
        case 0: return "NEW"
        case 1: return "ACKNOWLEDGED"
        default: return "ARCHIVED"
    }
}
def response(inn, List notifs) {
    return """<GetNotificationsResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <notificationsResponse>
    <inn>$inn</inn>
    ${notifs == null ? "" : notifs.findAll { 
        it.status == 0 || it.status == (ack ? 1 : -1) || it.status == (arch ? 2 : -1) 
    }.collect { 
        """<notif>
      <id>$it.id</id>
      <title>$it.title</title>
      <message>$it.text</message>
      <status>${status(it.status)}</status>
      <createdAt>${it.date}</createdAt>
      <updatedAt>${it.date}</updatedAt>
      <partnerId>string</partnerId>
    </notif>"""
    }}
  </notificationsResponse>
</GetNotificationsResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))