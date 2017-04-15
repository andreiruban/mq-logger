package io.ruban.api.rq;

/**
 * This class is an implementation of base request,
 * which will extended into many others despite client-server purposes.
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class MessageRequest {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
