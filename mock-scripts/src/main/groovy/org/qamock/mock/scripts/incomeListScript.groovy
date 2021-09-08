package org.qamock.mock.scripts

import groovy.time.TimeCategory

import java.text.SimpleDateFormat

//<GetIncomeRequestV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn>
// <From>2021-07-26T14:48:10.010Z</From><To>2021-07-30T00:00:00.000Z</To><Limit>100</Limit><Offset>0</Offset></GetIncomeRequestV2>

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest 
xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-
gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0"><Message><GetIncomeRequestV2 xmlns="urn://x-artefacts-gnivc-ru/ais3
/SMZ/SmzPartnersIntegrationService/types/1.0"><Inn>423050468026</Inn><From>2021-08-31T00:00:10.010Z</From><To>2021-08-31T23:00:00.000Z
</To><Limit>1</Limit><Offset>2</Offset></GetIncomeRequestV2></Message></SendMessageRequest></soap:Body></soap:Envelope>"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)

context.get("423050468026").put("incomes", [
        [recId: "receiptId${gen.number(8)}", type: "FROM_INDIVIDUAL", operTime: use(TimeCategory){ new Date() - 3.hours }, serv: [
                [name: "serv1", quant: "1", amount: "343.34"]
        ]],
        [recId: "receiptId${gen.number(8)}", type: "FROM_LEGAL_ENTITY", operTime: use(TimeCategory){ new Date() - 2.hours }, serv: [
                [name: "serv2", quant: "3", amount: "140.43"],
                [name: "serv3", quant: "3", amount: "160.43"]
        ]],
        [recId: "receiptId${gen.number(8)}", type: "FROM_INDIVIDUAL", operTime: use(TimeCategory){ new Date() - 1.hours }, cancTime: use(TimeCategory){ new Date() - 2.hours }, serv: [
                [name: "serv4", quant: "1", amount: "343.34"]
        ]]
])
        .put("reg", 1).put("bind", 1)

//---START RESPONSE SCRIPT---//
def req = params.get("request")

def inc = utils.xml(req).Body.SendMessageRequest.Message.GetIncomeRequestV2

pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

def inn = inc.Inn.text()
def from = Date.parse(pattern, inc.From.text())
def to = Date.parse(pattern, inc.To.text())
def limit = inc.Limit.text().toInteger()
def offset = inc.Offset.text().toInteger()

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
    if (!user.params.containsKey("incomes")) {
        user.params["incomes"] = [ ]
    }
    def incomes = user.params["incomes"].findAll {
        it.operTime >= from && it.operTime <= to
    }.toList()
    offset = offset > incomes.size() ? incomes.size() : offset
    def filtered = incomes.subList(offset, incomes.size()).take(limit)
    def receipts = filtered.collect { getIncomeXml(it,inn) }.join("\r\n")
    def more = !incomes.isEmpty() && !filtered.isEmpty() && incomes.last().recId != filtered.last().recId
    params.put("Message", response(receipts, more))
}

def getIncomeXml(inc, inn) {
    return """<Receipts>
    <Link>https://lknpd-adp.gnivc.ru/api/v1/receipt/$inn/${inc.recId}/print</Link>
    <TotalAmount>${inc.serv.sum { it.amount.toBigDecimal() }}</TotalAmount>
    <ReceiptId>${inc.recId}</ReceiptId>
    <IncomeType>${inc.type}</IncomeType>
    <RequestTime>${inc.operTime.format(pattern)}</RequestTime>
    <OperationTime>${inc.operTime.format(pattern)}</OperationTime>
    <TaxPeriodId>string</TaxPeriodId>
    <TaxToPay>194.76</TaxToPay>
    <PartnerCode>string</PartnerCode>
    <SupplierInn>string</SupplierInn>
    ${inc.cancTime == null ? '' : "<CancelationTime>${inc.cancTime.format(pattern)}</CancelationTime>"}
    ${inc.serv.collect { getServiceXml(it) }.join('') }
  </Receipts>"""
}

def getServiceXml(serv) {
    return """<Services>
      <Amount>${serv.amount}</Amount>
      <Name>${serv.name}</Name>
      <Quantity>${serv.quant}</Quantity>
    </Services>"""
}

def response(receipts, more) {
    return """<GetIncomeResponseV2 xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersValidationService/types/1.0" xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
  <HasMore>$more</HasMore>
  $receipts
</GetIncomeResponseV2>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))

println(params["Receipts"])
