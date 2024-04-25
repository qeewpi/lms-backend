package com.it120p.librarymanagementsystem.payload.response;

/**
 * The MessageResponse class is a model for the data returned as a response message.
 * It contains a single field for the message.
 *
 * The MessageResponse class is used by the controllers to send a response message to the client.
 *
 * The message is a string that represents the response message to be sent.
 */
public class MessageResponse {
    private String message;

    /**
     * Constructor for the MessageResponse with a message parameter.
     *
     * @param message the response message to be set.
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}