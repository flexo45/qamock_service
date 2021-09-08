package org.qamock.mock.scripts

import groovy.json.JsonSlurper

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()

//---START RESPONSE SCRIPT---//
def regions = new JsonSlurper().parseText(utils.fileString("regions.json"))
params.put("Message", response(regions))

def response(regions) {
    return """<GetRegionsListResponse xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    ${regions.collect {
        """<Regions>
    <Oktmo>$it.oktmo</Oktmo>
    <Name>$it.name</Name>
  </Regions>"""
    }.join("\r\n")}
</GetRegionsListResponse>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))