package org.task.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing a product.
 * This class is annotated with `@Entity` to indicate that it is a JPA entity.
 * It also uses Lombok's `@Data` annotation to generate getters, setters, constructors,
 * `equals`, `hashCode`, and `toString` methods at compile time.
 */
@Entity
@Data
public class Product {

    /**
     * The unique identifier of the product.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    /**
     * The unique Stock Keeping Unit (SKU) of the product.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "SKU is mandatory")
    @NotNull(message = "SKU must not be null")
    private String sku;

    /**
     * The name of the product.
     */
    @NotBlank(message = "Name is mandatory")
    private String name;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The category of the product.
     */
    private String category;

    /**
     * The price of the product.
     */
    @NotNull(message = "Price must not be null")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Digits(integer = 10,
            fraction = 2,
            message = "Quantity must be a number with up to 10 integer digits and 2 fractional digits")
    private Double price;

    /**
     * The quantity of the product.
     */
    @NotNull(message = "Quantity must not be null")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity;

    /**
     * The date and time when the product was created.
     * It is automatically set to the current date and time when the product is persisted.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    /**
     * The date and time when the quantity of the product was last changed.
     * It is automatically set to the current date and time when the product is persisted.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastQuantityChange;

    /**
     * Lifecycle callback method that gets triggered before the entity is persisted.
     * It sets the `createdAt` and `lastQuantityChange` fields to the current date and time.
     */
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        lastQuantityChange = LocalDateTime.now();
    }
}