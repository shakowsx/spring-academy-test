package org.task.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.task.model.Product;
import org.task.repositories.ProductRepository;
import org.task.services.ProductService;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing product-related operations.
 * This class is annotated with `@Service` to indicate that it is a service component in the Spring application context.
 */
@Service
public class ProductServiceImpl implements ProductService {

    /**
     * The ProductRepository instance for interacting with the database.
     * This field is automatically injected by Spring's dependency injection mechanism.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the given ID, or null if no such product exists.
     */
    @Override
    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Saves a product to the database.
     * If a DataIntegrityViolationException is thrown due to a ConstraintViolationException,
     * a DuplicateKeyException is thrown with a message indicating the duplicate SKU value.
     *
     * @param product The product to save.
     * @return The saved product.
     * @throws
     * DuplicateKeyException If a DataIntegrityViolationException is thrown with a ConstraintViolationException cause.
     * @throws
     * DataIntegrityViolationException If any other DataIntegrityViolationException is thrown.
     */
    @Override
    public Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateKeyException("Duplicate SKU value: " + product.getSku());
            } else {
                throw e;
            }
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     */
    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}