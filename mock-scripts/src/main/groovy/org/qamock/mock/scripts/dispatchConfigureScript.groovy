package org.qamock.mock.scripts

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()
request = new Request()

//---START DISPATCH SCRIPT---//
def json = request.jobject()
if (json.inn == null && json.params == null && json.params.empty) {
    return "bad_request"
}
params.put("inn", json.inn)
params.put("editable_params", json.params)
return "put_by_inn"
//---END DISPATCH SCRIPT---//

//---COMMON---//
class Request {
    def jobject() {
        return [ inn: "345352332", params: [ bind: "1", reg: "1" ] ]
    }
}