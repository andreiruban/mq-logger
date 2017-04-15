package io.ruban.api.rp.util;

import io.ruban.api.rp.BaseResponse;
import io.ruban.api.rp.DeliveredDefaultQueueResponse;
import io.ruban.api.rp.ErrorResponse;
import io.ruban.api.rp.SuccessResponse;
import io.ruban.api.rp.enums.ResponseTypes;
import io.ruban.api.rp.MessageNotProvidedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * This class is an implementation of response generator
 * which creates the response despite response type
 *
 * @author Andrei Ruban - software engineer IBA IT Park Minsk.
 * @version 30.06.2015
 */

@Component
@PropertySource(value = {"classpath:response_messages.properties"})
public class ResponseGenerator {

    public static final String SUCCESS_RESPONSE_MESSAGE_KEY = "message.delivered.successfully";
    public static final String MESSAGE_DELIVERED_DEFAULT_QUEUE_KEY = "message.delivered.default.queue";
    public static final String MESSAGE_NOT_PROVIDED_RESPONSE_MESSAGE_KEY = "message.not.provided";
    public static final String ERROR_RESPONSE_MESSAGE_KEY = "message.not.delivered";

    @Autowired
    private Environment environment;

    public BaseResponse generateResponse(ResponseTypes messageType) {

        BaseResponse response;
        if (messageType == ResponseTypes.SUCCESS_RESPONSE) {
            response = new SuccessResponse();
            response.setMessage(environment.getRequiredProperty(SUCCESS_RESPONSE_MESSAGE_KEY));
        } else if (messageType == ResponseTypes.DELIVERED_DEFAULT_QUEUE) {
            response = new DeliveredDefaultQueueResponse();
            response.setMessage(environment.getRequiredProperty(MESSAGE_DELIVERED_DEFAULT_QUEUE_KEY));
        } else if (messageType == ResponseTypes.MESSAGE_NOT_PROVIDED) {
            response = new MessageNotProvidedResponse();
            response.setMessage(environment.getRequiredProperty(MESSAGE_NOT_PROVIDED_RESPONSE_MESSAGE_KEY));
        } else {
            response = new ErrorResponse();
            response.setMessage(environment.getRequiredProperty(ERROR_RESPONSE_MESSAGE_KEY));
        }

        return response;
    }
}
