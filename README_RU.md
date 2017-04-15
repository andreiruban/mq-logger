MQ-Logger - модуль для работы с IBM Websphere MQ, предназначен для отсылки сообщений в очереди MQ.
Есть два режима работы с IBM Websphere MQ: binding-mode и client-mode.

Binding-mode можно использовать, когда Websphere MQ и mq-logger находятся на одной виртуальной машине. Упрощена авторизация и система безопасности.
Во всех остальных случаях используется client-mode.

Особенностью работы данного модуля является настройка объекта-получателя запросов (listener), который может слушать определенные очереди и генерировать отчеты,
которые затем осылает в другие очереди.

Для примера работы с модулем IBM Websphere MQ созданы rest и servlet api.


НАСТРОЙКА IBM Webspere MQ v. 7.5 

Администрирование и конфигурирование IBM Webspere MQ v. 7.5 можно производить как в IBM Webspere MQ Explorer, так и в командной строке. 
Ниже представлен вариант работы в командной строке.

1. После установки IBM Webspere MQ v. 7.5 необходимо создать объект посредник(broker) или администратор очередей (Queue Manager).
Введите: CRTMQM QM01, где QM01 - имя администратора очередей.

2. После создания менеджера очередей QM01.
Введите: STRMQM QM01

3. Для конфигурации менеджера очередей QM01, необходимо войти в меню MQSC-команд.
Введите: RUNMQSC QM01

4. В меню MQSC необходимо настроить сами очереди:

DEFINE QLOCAL(TEST.INPUT1)
DEFINE QLOCAL(TEST.INPUT2)
DEFINE QLOCAL(TEST.OUTPUT1)
 
Примечание: команда DEFINE - необходимо для создания объектов в IBM Webspere MQ, QLOCAL - указывает, что объектом является локальная очередь, а TEST.INPUT1 - имя очереди.

4. Далее в меню MQSC необходимо настроить получатель запросов (Listener) и запустить его.
Введите: DEFINE LISTENER(QM01.LISTENER) TRPTYPE (TCP) PORT(1414), где DEFINE - создать, LISTENER - получатель запросов, QM01.LISTENER - имя получателя запросов, TRPTYPE (TCP) - тип передачи - TCP, PORT(1414) - порт 1414.

Введите: START LISTENER(QM01.LISTENER) - команда запуска получателя запросов с именем QM01.LISTENER.

5. Для работы в клиентском режиме в меню MQSC необходимо настроить канал соединения с сервером (server-connection channel).
Введите: DEFINE CHANNEL(CHAN1) CHLTYPE(SVRCONN) TRPTYPE(TCP), где CHANNEL(CHAN1) - тип объекта, канал с именем CHAN1, CHLTYPE(SVRCONN) - тип канала, в данном случае - SVRCONN, что означает канал соединения типа server-connection.

6. В IBM Webspere MQ v. 7.5 существует авторизация клиентов. Для организации доступа к администратору очередей необходимо ввести следующую команду.
Введите: SET CHLAUTH(CHAN1) TYPE(ADDRESSMAP) ADDRESS(’IP address’) MCAUSER(’userid’), где CHLAUTH(CHAN1) - указывает на авторизацию канала с именем CHAN1, TYPE(ADDRESSMAP) - авторизация типа ADDRESSMAP, ADDRESS(’IP address’) - IP-адрес клиента, MCAUSER(’userid’) - имя пользователя менеджера очередей.

Примечание, иногда при работе с клиентом имеют место быть следующие ошибки: 
1) WebSphere MQ call failed with compcode '2' ('MQCC_FAILED') reason '2009' ('MQRC_CONNECTION_BROKEN') 
--> Connection to host '127.0.0.1(1414)' rejected 
-->  A communications error for 'TCP' occurred.

    Данная ошибка означает что клиент не может установить соединение. Возможно, что порт 1414 занят. поробуйте настроить получатель запросов с другим портом, например, 1415.

    После конфигурирования желательно перезапустите менеджер очередей и получатель запросов. 

MQRC 2035 (MQRC_NOT_AUTHORIZED)

    Данная ошибка возникает когда клиент не авторизован в системе. Настройте авторизацию (пункт 6) либо вовсе отключите ёё.
    ALTER QMGR CHLAUTH(DISABLED)