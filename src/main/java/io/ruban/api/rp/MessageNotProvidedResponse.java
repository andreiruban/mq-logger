package io.ruban.api.rp;

import io.ruban.api.rp.enums.ResponseTypes;

/**
 * This class is an implementation of message is not provided
 * response with ResponseType MESSAGE_NOT_PROVIDED
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class MessageNotProvidedResponse extends BaseResponse {

    private ResponseTypes messageType = ResponseTypes.MESSAGE_NOT_PROVIDED;

    public ResponseTypes getMessageType(){
        return messageType;
    }
}
