package org.qamock.mock.scripts

def x = """<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><SendMessageRequest xmlns="urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0" xmlns:ns2="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
    <Message><PostIncomeRequestV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0">
<Inn>423050468026</Inn><RequestTime>2021-08-16T10:18:59.000Z</RequestTime><OperationTime>2021-07-26T16:41:47.000Z</OperationTime>
<IncomeType>FROM_FOREIGN_AGENCY</IncomeType><CustomerOrganization>Ivanovo Trade LTD</CustomerOrganization>
<Services><Amount>200.00</Amount><Name>Затирка стен</Name><Quantity>1</Quantity></Services><Services><Amount>399.99</Amount><Name>Укладка плитки</Name><Quantity>1</Quantity></Services>
<TotalAmount>599.99</TotalAmount><OperationUniqueId>2dbf6189-8f38-4ce9-948f-407c33438443</OperationUniqueId></PostIncomeRequestV2></Message>
</SendMessageRequest></soap:Body></soap:Envelope>"""

def response = """<PostIncomeResponseV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <ReceiptId>\${#params#recId}</ReceiptId>
  <Link>https://lknpd-adp.gnivc.ru/api/v1/receipt/\${#params#inn}/\${#params#recId}/print</Link>
</PostIncomeResponseV2>"""

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
params.put("request", x)
context.get("423050468026").put("reg", 1)

def req = params.get("request")

pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

def inc = utils.xml(req).Body.SendMessageRequest.Message.PostIncomeRequestV2
def inn = inc.Inn.text()
def operTime = Date.parse(pattern, inc.OperationTime.text())
def type = inc.IncomeType.text()
def services = inc.Services

if (context.get(inn).params.reg == 1) {
    // OK
    def user = context.get(inn)
    // TODO Date filter
    if (!user.params.containsKey("incomes")) {
        user.params["incomes"] = []
    }
    def incomes = user.params["incomes"]
    def receiptId = "receiptId${gen.number(8)}"
    def income = [recId:receiptId, type: type, operTime: operTime, serv: []]
    services.each {
        income.get("serv").add([name: it.Name.text(), quant: it.Quantity.text(), amount: it.Amount.text()])
    }
    incomes.add(income)

    params.put("recId", receiptId)
    params.put("inn", inn)
} else {
    // ERROR
}
// [recId:"", type: "", operTime: "", cancTime: "", serv: [[name: "", quant: "", amount: ""]]]

println(context.get("423050468026").params)