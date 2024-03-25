package org.task.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.task.model.Product;

/**
 *  Class representing a response to a product request.
 *  Used to transmit a message and product data.
 */
@Data
@AllArgsConstructor
public class ProductResponse {

    /**
     * The message associated with the product request response.
     */
    private String message;

    /**
     * The product data to be included in the response.
     */
    private Product product;
}