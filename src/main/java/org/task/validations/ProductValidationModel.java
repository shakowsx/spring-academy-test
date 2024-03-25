package org.task.validations;

import lombok.Data;
import org.task.model.Product;

import javax.validation.constraints.*;

/**
 * Data transfer object (DTO) for validating product data.
 * This class is annotated with `@Data` from Lombok to generate getters, setters, constructors,
 * `equals`, `hashCode`, and `toString` methods at compile time.
 */
@Data
public class ProductValidationModel {
    /**
     * The unique identifier(UUID) of the product.
     * It must not be null and must match the UUID format.
     * This validation group is {@link UUIDValidation}.
     */
    @NotNull(message = "UUID must not be null", groups = UUIDValidation.class)
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid UUID format",
            groups = UUIDValidation.class)
    private String id;

    /**
     * The unique Stock Keeping Unit (SKU) of the product.
     * It must not be null and must be unique across all products.
     */
    @NotBlank(message = "SKU is mandatory")
    @NotNull(message = "SKU must not be null")
    private String sku;

    /**
     * The name of the product.
     * It must not be blank.
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
     * It must not be null and must be a positive number or zero.
     */
    @NotNull(message = "Price must not be null")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Digits(integer = 10,
            fraction = 2,
            message = "Quantity must be a number with up to 10 integer digits and 2 fractional digits")
    private Double price;

    /**
     * The quantity of the product.
     * It must not be null and must be a positive number or zero.
     */
    @NotNull(message = "Quantity must not be null")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity;

    /**
     * Converts this DTO to a Product entity.
     *
     * @return A new Product entity with the same data as this DTO.
     */
    public Product toProduct() {
        Product product = new Product();
        product.setSku(this.sku);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setCategory(this.category);
        product.setPrice(this.price);
        product.setQuantity(this.quantity);
        return product;
    }

    /**
     * Validation group interface for validating UUID.
     */
    public interface UUIDValidation {
    }
}