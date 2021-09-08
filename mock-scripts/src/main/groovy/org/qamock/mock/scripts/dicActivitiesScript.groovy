package org.qamock.mock.scripts

import groovy.json.JsonSlurper

context = Wrapper.shared().getContext(Init.fns)
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()

//---START RESPONSE SCRIPT---//
def activities = new JsonSlurper().parseText(utils.fileString("activities.json"))
params.put("Message", response(activities))

def response(activities) {
    return """<GetActivitiesListResponseV2 xmlns="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0" xsi:schemaLocation="urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0 schema.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    ${activities.collect {
        """<Activities>
    <Id>$it.id</Id>
${it.type == "child" ? "<ParentId>$it.parentId</ParentId>" : ""}
    <Name>$it.name</Name>
    <IsActive>1</IsActive>
  </Activities>"""
    }.join("\r\n")}
  <Activities>
    <Id>3453453453</Id>
    <Name>Неактивная активность</Name>
    <IsActive>0</IsActive>
  </Activities>

</GetActivitiesListResponseV2>"""
}
//---END RESPONSE SCRIPT---//
println(FnsMock.getFnsResponse(params.Message))