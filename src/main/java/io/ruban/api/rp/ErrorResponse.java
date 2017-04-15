package io.ruban.api.rp;

import io.ruban.api.rp.enums.ResponseTypes;

/**
 * This class is an implementation of error response with ResponseType ERROR_RESPONSE
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class ErrorResponse extends BaseResponse {
    private ResponseTypes messageType = ResponseTypes.ERROR_RESPONSE;

    public ResponseTypes getMessageType(){
        return messageType;
    }
}
