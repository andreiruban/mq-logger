package io.ruban.api.rq;

/**
 * This class is an implementation of MessageQueueRequest,
 * as extension of MessageRequest.
 *
 * @author Andrei Ruban - software engineer.
 * @version 30.06.2015
 */

public class MessageQueueRequest extends MessageRequest {

    private String queueName;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
