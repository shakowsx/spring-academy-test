package org.task.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.task.model.Product;
import org.task.repositories.ProductRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @Order(1)
    void testGetAllProducts() {
        // Arrange
        List<Product> products = IntStream.range(0, 2)
                .mapToObj(i -> createTestProduct())
                .collect(Collectors.toList());
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(products, result);
    }

    @Test
    @Order(2)
    void testGetProductById() {
        // Arrange
        Product product = createTestProduct();
        UUID productId = product.getId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertEquals(product, result);
    }

    @Test
    @Order(3)
    public void testGetProductByIdNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNull(result);
    }

    @Test
    @Order(4)
    public void testSaveProduct() {
        // Arrange
        Product product = createTestProduct();
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.saveProduct(product);

        // Assert
        assertEquals(product, result);
    }

    @Test
    @Order(5)
    public void testDatesSetting() {
        // Arrange
        Product product = createTestProduct();
        LocalDateTime now = LocalDateTime.now();
        when(productRepository.save(eq(product))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.prePersist();
            return savedProduct;
        });

        // Act
        Product savedProduct = productService.saveProduct(product);

        // Assert
        assertNotNull(savedProduct.getCreatedAt());
        assertNotNull(savedProduct.getLastQuantityChange());
        assertThat(savedProduct.getCreatedAt()).isCloseTo(now, within(1, ChronoUnit.SECONDS));
        assertThat(savedProduct.getLastQuantityChange()).isCloseTo(now, within(1, ChronoUnit.SECONDS));
    }

    @Test
    @Order(6)
    public void testSaveProductWithDuplicateSku() {
        // Arrange
        Product product = createTestProduct();
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        DataIntegrityViolationException dataIntegrityViolationException =
                new DataIntegrityViolationException("Duplicate SKU", constraintViolationException);
        doThrow(dataIntegrityViolationException).when(productRepository).save(product);

        // Act and Assert
        assertThrows(DuplicateKeyException.class, () -> productService.saveProduct(product));
    }

    @Test
    @Order(7)
    void testDeleteProduct() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));

        // Act
        productService.deleteProduct(productId);

        // Assert #1
        verify(productRepository, times(1)).deleteById(productId);

        // Arrange override
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Assert #2
        assertFalse(productRepository.findById(productId).isPresent());
    }

    // Method for creating a test product
    private Product createTestProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setSku("TEST-SKU");
        product.setName("Test product");
        product.setDescription("Test description");
        product.setCategory("Test Category");
        product.setPrice(100.00);
        product.setQuantity(1000);
        return product;
    }
}