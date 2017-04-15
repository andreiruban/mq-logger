package io.ruban.api.rp;

import io.ruban.api.rp.enums.ResponseTypes;

/**
 * This class is an implementation of base response,
 * which will extended into many others despite ResponseType
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class BaseResponse {

    private ResponseTypes messageType;

    private String message;

    public ResponseTypes getMessageType() {
        return messageType;
    }

    public void setMessageType(ResponseTypes messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
