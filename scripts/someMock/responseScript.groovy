def params = [:]
//---START SCRIPT---//
params.put("message", "hello world!")
//---END SCRIPT---//
println(FnsMock.getFnsResponse(params.message))