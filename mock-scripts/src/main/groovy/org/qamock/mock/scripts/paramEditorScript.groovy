package org.qamock.mock.scripts

context = Wrapper.shared().getContext()
params = Wrapper.shared().getParams()
utils = Wrapper.shared().getUtils()
gen = Wrapper.shared().getGen()

context.get("423050468026")
        .put("info", [inn: "423050468026"])
        .put("reg", 1)
        .put("bind", 2)
        .put("perm", ["1", "2", "3"])
        .put("bindId", "6271b794-1fe3-4000-943b-75266c8a473d")

params.put("inn", "423050468026")
params.put("editable_params", [bind: 1])

//---START RESPONSE SCRIPT---//
def user = context.get(params.inn).params
params.'editable_params'.each {
    user.put(it.key, it.value)
}
//---END RESPONSE SCRIPT---//
println(context.get("423050468026").params)