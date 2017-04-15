package io.ruban.api.rp;

import io.ruban.api.rp.enums.ResponseTypes;

/**
 * This class is an implementation of success response with ResponseType SUCCESS_RESPONSE
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class SuccessResponse extends BaseResponse{
    private ResponseTypes messageType = ResponseTypes.SUCCESS_RESPONSE;

    public ResponseTypes getMessageType(){
        return messageType;
    }
}
