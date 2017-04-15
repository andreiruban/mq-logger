MQ-Logger is a module for working with IBM Websphere MQ, designed to send messages to the MQ queue.
There are two modes of working with IBM Websphere MQ: binding-mode and client-mode.

Binding-mode can be used when Websphere MQ and mq-logger are on the same virtual machine. Authorization and security are simplified.
In all other cases, client-mode is used.

The peculiarity of this module is the configuration of the object-listener, which can listen to certain queues and generate reports,
which then sends to other queues.

For example, working with the IBM Websphere MQ module, rest and servlet api are created.


SETTING UP IBM Webspere MQ v. 7.5

Administration and configuration of IBM Webspere MQ v. 7.5 can be produced both in the IBM Webspere MQ Explorer and in the command line.
Below is the option of working on the command line.

1. After installing IBM Webspere MQ v. 7.5, you need to create an intermediary object (broker) or queue manager (Queue Manager).
Type: CRTMQM QM01, where QM01 is the name of the queue administrator.

2. After the queue manager QM01 has been created.
Enter: STRMQM QM01

3. To configure the queue manager QM01, you must enter the MQSC command menu.
Enter: RUNMQSC QM01

4. In the MQSC menu, you must configure the queues themselves:

DEFINE QLOCAL (TEST.INPUT1)
DEFINE QLOCAL (TEST.INPUT2)
DEFINE QLOCAL (TEST.OUTPUT1)
 
Note: The DEFINE command is required to create objects in IBM Webspere MQ, QLOCAL indicates that the object is a local queue, and TEST.INPUT1 is the name of the queue.

4. In the MQSC menu, you must configure the listener and start it.
Enter: DEFINE LISTENER (QM01.LISTENER) TRPTYPE (TCP) PORT (1414), where DEFINE is created, LISTENER is the requestor, QM01.LISTENER is the name of the recipient of the request, TRPTYPE (TCP) is the transfer type is TCP, PORT (1414) Port 1414.

Type: START LISTENER (QM01.LISTENER) - the command to start the receiver of requests with the name QM01.LISTENER.

5. To work in the client mode, you need to configure the server-connection channel in the MQSC menu.
Enter: DEFINE CHANNEL (CHAN1) CHLTYPE (SVRCONN) TRPTYPE (TCP), where CHANNEL (CHAN1) is the type of the object, the channel named CHAN1, CHLTYPE (SVRCONN) is the channel type, in this case SVRCONN, which means the server connection channel -connection.

6. In IBM Webspere MQ v. 7.5 there is authorization of clients. To organize access to the queue administrator, you must enter the following command.
Enter: SET CHLAUTH (CHAN1) TYPE (ADDRESSMAP) ADDRESS ('IP address') MCAUSER ('userid'), where CHLAUTH (CHAN1) - indicates authorization of the channel named CHAN1, TYPE (ADDRESSMAP) - ADDRESSMAP, ADDRESS 'IP address') is the IP address of the client, MCAUSER ('userid') is the name of the queue manager user.

Note, sometimes when working with a client there are the following errors:
1) WebSphere MQ call failed with compcode '2' ('MQCC_FAILED') reason '2009' ('MQRC_CONNECTION_BROKEN')
-> Connection to host '127.0.0.1 (1414)' rejected
-> A communications error for 'TCP' occurred.

    This error indicates that the client can not establish a connection. It is possible that port 1414 is busy. Try setting up the requestor with another port, for example, 1415.

    After configuration, it is desirable to restart the queue manager and the requestor.

MQRC 2035 (MQRC_NOT_AUTHORIZED)

    This error occurs when the client is not authorized on the system. Configure the authorization (step 6) or completely disable it.
    ALTER QMGR CHLAUTH (DISABLED)