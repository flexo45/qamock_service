Сервис для создания заглушек/моков с динамическим описанием логики ответов на groovy. 

Содержит набор скриптов для эмуляции работы портала ФНС (по SOAP протоколу).

Для редактирования скриптов можно использовать админку http://host:port/admin (дефолтный пользователь admin/1234)

Endpoint для ФНС:
- Авторизация: http://host:port/mock/fns/npd/auth/v1
- Сервис: http://host:port/mock/fns/npd/usmz/v1

Предустановленные пользователи с КТИР-2 ФНС

<code>
user("423050468026", "Пронизывающий", "Платонович", "Коготок", "123", "+79347462332", "sdfg@mail.ru")<br>
user("621321480511", "Варварский", "Спинозович", "Столб", "1", "+79345433311", "adfgdf@mail.ru")<br>
user("630419157559", "Изгоняющий", "Талисман", "Демокритович", "123", "+79343456545", "sdfg@mail.ru")<br>
user("692175779201", "Старейшина", "Сократович", "Сумасбродный", "123", "+79349345445", "sdfg@mail.ru")<br>
</code>


