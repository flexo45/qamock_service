package org.qamock.mock.scripts

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()

def err = """<SmzPlatformError xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Code>\${#params#ErrCode}</Code>
  <Message>\${#params#ErrMessage}</Message>
</SmzPlatformError>"""

context.get("fns")
        .put("deny", false)
        .put("error", err)
        .put("err_ise", [code: "INTERNAL_ERROR", text: "Внутренняя ошибка ПП НПД"])
        .put("err_deny", [code: "PARTNER_DENY", text: "Партнеру отказано в осуществлении операции"])
        .put("err_rve", [code: "REQUEST_VALIDATION_ERROR", text: "Ошибка парсинга запроса"])
        .put("err_unreg", [code: "TAXPAYER_UNREGISTERED", text: "Пользователь снят с учета или не поставлен на учет"])
        .put("err_unbound", [code: "TAXPAYER_UNBOUND", text: "Пользователь не привязан к Партнеру"])

def raw = request.content()
def xml = new XmlSlurper().parseText(raw)

if (raw.contains("GetMessageRequest")) {
    if (context.get("fns_global").params.deny) {
//        context.get("fns_global").put("deny", false)
        params.put("err", "err_deny")
        return "fns_error"
    }

    if (context.get("fns_global").params.ise) {
//        context.get("fns_global").put("ise", false)
        params.put("err", "err_ise")
        return "fns_error"
    }

    if (context.get("fns_global").params.unavail) {
//        context.get("fns_global").put("unavail", false)
        sleep(6000)
        return "fns_error"
    }

    def messageId = xml.Body.GetMessageRequest.MessageId.text()
    utils.log("Found messageId: " + messageId)

    def action = getActionByMessageId(messageId)
    return action
}

def a = null
a = tryDo(a, raw, "PostPlatformRegistrationRequest", "platform")
a = tryDo(a, raw, "GetActivitiesListRequestV2", "dic_activities")
a = tryDo(a, raw, "GetRegionsListRequest", "dic_regions")
a = tryDo(a, raw, "GetRejectionReasonsListRequest", "dic_reject_reas")
a = tryDo(a, raw, "GetUnregistrationReasonsListRequest", "dic_unreg_reas")
a = tryDo(a, raw, "GetCancelIncomeReasonsListRequest", "dic_cancel_inc_reas")
a = tryDo(a, raw, "GetTaxpayerUnregistrationReasonsListRequest", "dic_tax_unreg_reas")

a = tryDo(a, raw, "GetInnByPersonalInfoRequestV3", "smz_inn")
a = tryDo(a, raw, "GetTaxpayerRestrictionsRequest", "smz_verification")
a = tryDo(a, raw, "PostRegistrationRequest", "smz_registration")
a = tryDo(a, raw, "GetRegistrationStatusRequest", "smz_registration_order")
a = tryDo(a, raw, "PostUnbindPartnerRequest", "smz_unbind")
a = tryDo(a, raw, "PostBindPartnerWithInnRequest", "smz_bind")
a = tryDo(a, raw, "GetBindPartnerStatusRequest", "smz_bind_order")
a = tryDo(a, raw, "PostUnregistrationRequestV2", "smz_unreg")
a = tryDo(a, raw, "GetUnregistrationStatusRequest", "smz_unreg_order")
a = tryDo(a, raw, "GetTaxpayerStatusRequest", "smz_status")
a = tryDo(a, raw, "PutTaxpayerDataRequest", "smz_update_taxpayer")
a = tryDo(a, raw, "GetAccrualsAndDebtsRequest", "smz_debt")
a = tryDo(a, raw, "GetGrantedPermissionsRequest", "smz_perm")
a = tryDo(a, raw, "GetNotificationsCountRequest", "smz_notif_count")
a = tryDo(a, raw, "GetNotificationsRequest", "smz_notif_list")
a = tryDo(a, raw, "PostNotificationsAckRequest", "smz_notif_ack")
a = tryDo(a, raw, "PostNotificationsDeliveredRequest", "smz_notif_deliv")
a = tryDo(a, raw, "PostNotificationsArchRequest", "smz_notif_arch")
a = tryDo(a, raw, "GetIncomeReferenceRequestV2", "smz_refs")

a = tryDo(a, raw, "GetIncomeRequestV2", "inc_list")
a = tryDo(a, raw, "GetIncomeRequestV2", "inc_list")
a = tryDo(a, raw, "PostIncomeRequestV2", "inc_reg")
a = tryDo(a, raw, "GetIncomeForPeriodRequest", "inc_period")
a = tryDo(a, raw, "PostCancelReceiptRequestV2", "inc_cancel")

a = tryDo(a, raw, "GetKeysRequest", "fns_get_keys")

//a = tryDo(a, raw, "", "")

return a

def getActionByMessageId(messageId) {
    def c = context.get(messageId)
    params.put("request", c.params["request"])
    utils.log("Found context " + messageId)
    c.params.each { utils.log(it.toString()) }
    return c.params["action"]
}

def putAction(action, raw) {
    def messageId = UUID.randomUUID().toString()
    params.put("messageId", messageId)
    utils.log("Found action = " + action + " for messageId: " + messageId )
    context.get(messageId)
            .put("action", action)
            .put("request", raw)
}

def tryDo(a, raw, method, action) {
    if (a != null) {
        utils.log("a is not empty: " + a)
        return a
    }
    if (raw.contains(method)) {
        putAction(action, raw)
        return "messageId"
    }
}



//---COMMON---//

class FnsMock {
    static def getFnsResponse(text) {
        return """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><GetMessageResponse xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0">
<ProcessingStatus>COMPLETED</ProcessingStatus>
<Message>
$text
</Message>
</GetMessageResponse></soap:Body></soap:Envelope>"""
    }
}

class Init {
    static def fns = [
            fns: [
                    error: """<SmzPlatformError xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Code>\${#params#ErrCode}</Code>
  <Message>\${#params#ErrMessage}</Message>
</SmzPlatformError>""",
                    err_ise: [code: "INTERNAL_ERROR", text: "Внутренняя ошибка ПП НПД"],
                    err_rve: [code: "REQUEST_VALIDATION_ERROR", text: "Ошибка парсинга запроса"]
            ]
    ]
}