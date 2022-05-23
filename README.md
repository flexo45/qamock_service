#Описание
Сервис для создания заглушек/моков с динамическим описанием логики ответов на groovy. 

#Настройка заглушек
##Админка
Создавать или редактировать эндпоинты заглушек и их скрипты можно через встроенную админку http://host:port/admin (дефолтный пользователь admin/1234)
##Автозагрузка
Можно настроить автозагрузку заглушек из специально описанного формата.

В корневой папке с jar файлом мок сервиса нужно создать папку /scripts

В этой папке создать еще одну, в которой будет размещаться файл с настройками заглушки settings.json и файлы со скриптами

*Пример*

    /scripts/someMock
    /scripts/someMock/settings.xml
    /scripts/someMock/dispatchScript.txt
    /scripts/someMock/responseScript.groovy

*Пример содержания файла settings.xml*

    {
        "name": "Some Mock",
        "path": "service/api/v1",
        "methods": [ "POST", "GET" ],
        "logging": true,
        "dispatch": "script",
        "dispatchScript": "dispatchScript.txt",
        "defaultResponse": "okResponse",
        "responses": [
            {
                "name": "scriptedResponse",
                "code": 200,
                "headers": [ "Content-Type: application/json; charset=utf-8" ],
                "body": "{\"message\": \"${#params#message}\"}",
                "prescript": "responseScript.groovy"
            },
            {
                "name": "okResponse",
                "code": 200,
                "headers": [ "Content-Type: application/json; charset=utf-8" ],
                "body": "{\"result\": \"OK\"}",
                "prescript": null
            }
        ]
    }

*Создастся заглушка, доступная по пути http://mock-server-host/mock/service/api/v1, которая ожидает запросы на методы*
*POST и GET. Заглушка будет обрабатывать запросы и отдавать ответы через скрипт (параметр dispatch), сам скрипт лежит в*
*файле dispatchScript.txt*

*У заглушки есть два варианта ответов, описанных в массиве responses*

Файл со скриптом может быть любым текстовым файлом, например просто .txt или файл с каким то groovy кодом, который можно 
разрабатывать в какой-то изолированной среде. Главное, что бы текст скрипты был обернут в ключевые метки 
    
    //---START SCRIPT---// 

и

    //---END SCRIPT---//

*Пример файла dispatchScript.txt*

    Тут какой то текст или описание скрипта

    //---START SCRIPT---//
    def clientId = request.jobject().get("id")
    params.put("message", clientId)
    if (clientId == "123") { return "scriptedResponse" } 
    else { return "okResponse" }
    //---END SCRIPT---//

    Тут может быть еще какое-то пояснение, в итоге попадет только текст между метками

*Пример файла responseScript.groovy*

    def params = [:]
    //---START SCRIPT---//
    params.put("message", "hello world!")
    //---END SCRIPT---//
    println(FnsMock.getFnsResponse(params.message))