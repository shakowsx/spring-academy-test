package org.task.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a message response to a request.
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {

    /**
     * The message associated with the request response.
     */
    private String message;


    /**
     * A list of additional details associated with the request response.
     * Can be null if there are no details.
     */
    private List<String> details;

    /**
     * Constructor that accepts only the message.
     * Used when there are no details.
     *
     * @param message The message associated with the request response.
     */
    public MessageResponse(String message) {
        this.message = message;
        // Initialize the details list as null by default to avoid creating it if not needed
        this.details = null;
    }

    /**
     * Constructor that accepts the message and a single detail.
     * Used when there is only one detail.
     *
     * @param message The message associated with the request response.
     * @param detail An additional detail associated with the request response.
     */
    public MessageResponse(String message, String detail) {
        this.message = message;
        this.details = new ArrayList<>();
        this.details.add(detail);
    }
}