package io.ruban.api.web;

import io.ruban.api.rp.BaseResponse;
import io.ruban.api.rp.enums.ResponseTypes;
import io.ruban.api.rp.util.ResponseGenerator;
import io.ruban.api.rq.MessageQueueRequest;
import io.ruban.service.mq.MQMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class provides is an implementation of Web API
 * of mq-logger webapp & describes bean definition of Spring JMS
 * & IBM Websphere MQ classes for JMS
 *
 * @author Andrei Ruban - software engineer.
 * @version 09.07.2015
 */

@Controller
@PropertySource(value = {"classpath:response_messages.properties"})
public class MessageController {

    @Autowired
    private ResponseGenerator responseGenerator;


    @Autowired
    private MQMessageSender mqMessageSender;

    /**
     * This method returns start page.
     *
     * @return mav a ModelAndView
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView welcome() {
        ModelAndView mav = new ModelAndView("welcome");
        mav.addObject("MessageQueueRequest", new MessageQueueRequest());
        mav.setViewName("sendpage");
        return mav;
    }

    /**
     * This method sends message to IBM Websphere MQ
     *
     * @param request a MessageQueueRequest
     * @param mav     a ModelAndView
     * @return mav a ModelAndView
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ModelAndView sendMessage(@ModelAttribute("MessageQueueRequest") MessageQueueRequest request, final ModelAndView mav) {

        String message = request.getMessage();
        String queueName = request.getQueueName();

        BaseResponse response;

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

        mav.addObject("response", response);
        mav.setViewName("sendpage");
        return mav;
    }
}
