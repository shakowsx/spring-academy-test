package org.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.task.model.Product;
import org.task.responses.MessageResponse;
import org.task.responses.ProductResponse;
import org.task.services.impl.ProductServiceImpl;
import org.task.validations.ProductValidationModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing product-related operations.
 * This controller handles HTTP requests and responses for product-related endpoints.
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    /**
     * The ProductServiceImpl instance for interacting with the product.
     * This field is automatically injected by Spring's dependency injection mechanism.
     */
    @Autowired
    private ProductServiceImpl productService;

    /**
     * Retrieves a list of all products.
     *
     * @return A {@link ResponseEntity} with the following status codes:
     * <ul>
     *     <li>200 OK: If there are products found. The body of the response will contain a list of {@link Product}
     *     objects.</li>
     *     <li>200 OK: If there are no products found. The body of the response will contain a {@link MessageResponse}
     *     with a "No products found" message.</li>
     *     <li>500 Internal Server Error: If an unexpected error occurs during the retrieval operation. The body of
     *     the response will contain a {@link MessageResponse} with an error message and the details of the
     *     exception.</li>
     * </ul>
     * @throws RuntimeException If there is an unexpected error during the retrieval operation. The exception message
     *                          will contain the details of the error.
     */
    @GetMapping("/products")
    @Operation(summary = "Retrieve all products", description = "Returns a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "200", description = "No products found"),
            @ApiResponse(responseCode = "500", description = "An error occurred while retrieving the products")
    })
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (!products.isEmpty()) {
                return ResponseEntity.ok(products);
            } else {
                return ResponseEntity.ok(new MessageResponse("No products found."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MessageResponse("An error occurred while retrieving the products.", e.getMessage()));
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productValidationModel The {@link ProductValidationModel} containing the product ID.
     *                               The ID is used to identify the product to be retrieved.
     * @return A {@link ResponseEntity} with the following status codes:
     * <ul>
     *     <li>200 OK: If the product is found. The body of the response will contain the {@link Product} object.</li>
     *     <li>404 Not Found: If the product with the provided ID does not exist. The body of the response will contain
     *     a {@link MessageResponse} with a "Product not found" message.</li>
     *     <li>500 Internal Server Error: If there is an error during the retrieval operation. The body of the response
     *     will contain a {@link MessageResponse} with an error message.</li>
     * </ul>
     * @throws IllegalArgumentException If the provided product ID is not a valid UUID.
     */
    @GetMapping("/products/:id")
    @Operation(summary = "Retrieve a product by ID", description = "Returns a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "An error occurred while retrieving the product")
    })
    public ResponseEntity<?> getProductById(
            @Validated(ProductValidationModel.UUIDValidation.class)
            @RequestBody ProductValidationModel productValidationModel) {
        try {
            UUID id = UUID.fromString(productValidationModel.getId());
            Product product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new MessageResponse("Product with id " + id + " not found."));
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MessageResponse("An error occurred while retrieving the product.", e.getMessage()));
        }
    }

    /**
     * Saves a new product to the system.
     *
     * @param product The {@link Product} object to be saved. This object should contain all the necessary details
     *                for creating a new product.
     * @return A {@link ResponseEntity} with the following status codes:
     * <ul>
     *     <li>200 OK: If the product is successfully saved. The body of the response will contain a
     *     {@link ProductResponse} with a success message and the saved {@link Product} object.</li>
     *     <li>400 Bad Request: If there is an error during the save operation. The body of the response will contain a
     *     {@link MessageResponse} with an error message.</li>
     * </ul>
     * @throws javax.validation.ConstraintViolationException If the provided {@link Product} object does not meet the
     *                                                       validation constraints defined on its fields.
     */
    @Operation(summary = "Save a new product", description = "Saves a new product to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved product"),
            @ApiResponse(responseCode = "500", description = "An error occurred while saving the product")
    })
    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(
                    new ProductResponse("Product created successfully.", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Failed to create product.", e.getMessage()));
        }
    }

    /**
     * Updates an existing product with the provided details.
     *
     * @param productValidationModel The {@link ProductValidationModel} containing the product ID and the updated
     *                               details.
     *                               The ID is used to identify the product to be updated, and the details are used to
     *                               update the product's fields.
     * @return A {@link ResponseEntity} with the following status codes:
     * <ul>
     *     <li>200 OK: If the product is successfully updated. The body of the response will contain a
     *     {@link ProductResponse} with a success message and the updated {@link Product} object.</li>
     *     <li>400 Bad Request: If there is an error during the update operation. The body of the response will contain
     *     a {@link MessageResponse} with an error message.</li>
     *     <li>404 Not Found: If the product with the provided ID does not exist. The body of the response will contain
     *     a {@link MessageResponse} with a "Product not found" message.</li>
     * </ul>
     * @throws IllegalArgumentException If the provided product ID is not a valid UUID.
     */
    @PutMapping("/products")
    @Operation(summary = "Update an existing product", description = "Updates a product in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated product"),
            @ApiResponse(responseCode = "400", description = "An error occurred while updating the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductValidationModel productValidationModel) {
        try {
            UUID id = UUID.fromString(productValidationModel.getId());
            Product existingProduct = productService.getProductById(id);

            if (existingProduct != null) {
                Product product = productValidationModel.toProduct();
                product.setId(id);
                product.setCreatedAt(existingProduct.getCreatedAt());

                if (!existingProduct.getQuantity().equals(product.getQuantity())) {
                    product.setLastQuantityChange(LocalDateTime.now());
                } else {
                    product.setLastQuantityChange(existingProduct.getLastQuantityChange());
                }

                Product updatedProduct = productService.saveProduct(product);
                return ResponseEntity.ok(
                        new ProductResponse("Product updated successfully.", updatedProduct));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new MessageResponse("Product with id " + id + " not found."));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Failed to update product.", e.getMessage()));
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param productValidationModel The {@link ProductValidationModel} containing the product ID.
     *                               The ID is used to identify the product to be deleted.
     * @return A {@link ResponseEntity} with the following status codes:
     * <ul>
     *     <li>200 OK: If the product is successfully deleted. The body of the response will contain a
     *     {@link MessageResponse} with a success message.</li>
     *     <li>400 Bad Request: If there is an error during the delete operation. The body of the response will contain
     *     a {@link MessageResponse} with an error message.</li>
     *     <li>404 Not Found: If the product with the provided ID does not exist. The body of the response will contain
     *     a {@link MessageResponse} with a "Product not found" message.</li>
     * </ul>
     * @throws IllegalArgumentException If the provided product ID is not a valid UUID.
     */
    @DeleteMapping("/products")
    @Operation(summary = "Delete a product by ID", description = "Deletes a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted product"),
            @ApiResponse(responseCode = "400", description = "An error occurred while deleting the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<MessageResponse> deleteProduct (
            @Validated(ProductValidationModel.UUIDValidation.class)
            @RequestBody ProductValidationModel productValidationModel) {
        UUID id = UUID.fromString(productValidationModel.getId());
        Product product = productService.getProductById(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse("Product with id " + id + " not found."));
        } else {
            try {
                productService.deleteProduct(id);
                return ResponseEntity.ok(
                        new MessageResponse("Product with id " + id + " has been deleted successfully."));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                        new MessageResponse("Failed to delete product with id " + id + ": " + e.getMessage()));
            }
        }
    }
}