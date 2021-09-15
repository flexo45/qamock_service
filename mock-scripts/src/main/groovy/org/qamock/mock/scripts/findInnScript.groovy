package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest 
xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-
gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><GetInnByPersonalInfoRequestV3 xmlns="urn://x-artefacts-
gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><PersonalInfoList> <FirstName>Платонович</FirstName><SecondName>Пронизывающий</SecondName>
<Patronymic>Сократович</Patronymic><Birthday>1981-02-17Z</Birthday><DocumentSpdul>21</DocumentSpdul><DocumentSeries>1303</DocumentSeries>
<DocumentNumber>592940</DocumentNumber></PersonalInfoList></GetInnByPersonalInfoRequestV3></Message></SendMessageRequest></soap:Body></soap:Envelope>"""
context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)

// <FirstName>Принцип</FirstName>
// <SecondName>Существенный</SecondName>
// <Patronymic>Сократович</Patronymic>
// <Birthday>1981-02-17Z</Birthday>
// <DocumentSpdul>21</DocumentSpdul>
// <DocumentSeries>1303</DocumentSeries>
// <DocumentNumber>592940</DocumentNumber>

//---START RESPONSE SCRIPT---//
def users = [
        user("423050468026", "Пронизывающий", "Платонович", "Коготок", "123", "+79347462332", "sdfg@mail.ru"),
        user("621321480511", "Варварский", "Спинозович", "Столб", "1", "+79345433311", "adfgdf@mail.ru"),
        user("630419157559", "Изгоняющий", "Талисман", "Демокритович", "123", "+79343456545", "sdfg@mail.ru"),
        user("692175779201", "Старейшина", "Сократович", "Сумасбродный", "123", "+79349345445", "sdfg@mail.ru")
]

def req = params.get("request")

def pers = utils.xml(req).Body.SendMessageRequest.Message.GetInnByPersonalInfoRequestV3
        .PersonalInfoList

def user = users.find{ it.last == pers.SecondName.text() && it.first == pers.FirstName.text() }

if (user == null) {
    params.put("Message", response(null))
} else {
    initUser(user)
    params.put("Message", response(user.inn))
}

def user(inn, last, first, mid, doc, phone, mail) {
    return [inn: inn, last: last, first: first, mid: mid, doc: doc, phone: phone, mail: mail]
}

def initUser(user) {
    if (context.get(user.inn).params.reg == null) {
        context.get(user.inn).put("reg", 0).put("bind", 0)
    }
    context.get(user.inn)
            .put("info", user)
}

def response(inn) {
    return """<GetInnByPersonalInfoResponseV3 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <InnList>
    ${inn == null ? "" : "<Inn>$inn</Inn>"}
  </InnList>
</GetInnByPersonalInfoResponseV3>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))