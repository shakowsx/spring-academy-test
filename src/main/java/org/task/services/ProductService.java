package org.task.services;

import org.springframework.dao.DuplicateKeyException;
import org.task.model.Product;

import java.util.List;
import java.util.UUID;

/**
 * Interface for managing product-related operations.
 */
public interface ProductService {

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the given ID, or null if no such product exists
     */
    Product getProductById(UUID id);

    /**
     * Saves a product.
     *
     * @param product the product to save
     * @return the saved product
     * @throws DuplicateKeyException if a product with the same SKU already exists
     */
    Product saveProduct(Product product) throws DuplicateKeyException;

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    void deleteProduct(UUID id);
}