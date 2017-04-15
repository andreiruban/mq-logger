package io.ruban.api.rest;

import io.ruban.api.rp.BaseResponse;
import io.ruban.api.rp.enums.ResponseTypes;
import io.ruban.api.rp.util.ResponseGenerator;
import io.ruban.api.rq.MessageQueueRequest;
import io.ruban.service.mq.MQMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides is an implementation of REST API
 * of mq-logger webapp & describes bean definition of Spring JMS
 * & IBM Websphere MQ classes for JMS.
 *
 * @author Andrei Ruban - software engineer.
 * @version 09.07.2015
 */

@RestController
@RequestMapping(value = "/messageService")
public class MessageService {

    @Autowired
    private ResponseGenerator responseGenerator;

    @Autowired
    private MQMessageSender mqMessageSender;

    /**
     * This method sends message to IBM Websphere MQ.
     * This method requires two parameters: message & queueName.
     * If queueName is missing the method sends message to default queue.
     *
     * @param request a MessageQueueRequest
     * @return result a Object
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public BaseResponse sendMessage(MessageQueueRequest request) {

        BaseResponse response;

        String message = request.getMessage();
        String queueName = request.getQueueName();

        if (message != null && queueName != null) {
            mqMessageSender.send(message, queueName);
            response = responseGenerator.generateResponse(ResponseTypes.SUCCESS_RESPONSE);
        } else if (queueName == null) {
            mqMessageSender.send(message);
            response = responseGenerator.generateResponse(ResponseTypes.DELIVERED_DEFAULT_QUEUE);
        } else if (message == null) {
            response = responseGenerator.generateResponse(ResponseTypes.MESSAGE_NOT_PROVIDED);
        } else {
            response = responseGenerator.generateResponse(ResponseTypes.ERROR_RESPONSE);
        }

        return response;
    }
}
