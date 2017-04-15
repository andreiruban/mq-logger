package io.ruban.api.rp;

import io.ruban.api.rp.enums.ResponseTypes;

/**
 * This class is an implementation of message delivered
 * to default queue response with ResponseType DELIVERED_DEFAULT_QUEUE
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class DeliveredDefaultQueueResponse extends BaseResponse{
    private ResponseTypes messageType = ResponseTypes.DELIVERED_DEFAULT_QUEUE;

    public ResponseTypes getMessageType(){
        return messageType;
    }
}
