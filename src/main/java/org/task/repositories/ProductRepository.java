package org.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.model.Product;

import java.util.UUID;

/**
 * Repository interface for managing {@link Product} entities in the database.
 * This interface extends {@link JpaRepository}, which provides basic CRUD operations
 * and allows for custom queries using Spring Data JPA.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // No additional comments are needed as the JpaRepository methods are self-explanatory.
}