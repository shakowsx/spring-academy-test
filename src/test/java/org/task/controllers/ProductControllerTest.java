package org.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.task.model.Product;
import org.task.services.impl.ProductServiceImpl;
import org.task.validations.ProductValidationModel;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Test
    @Order(1)
    void whenUpdateProductAndQuantityChangedShouldUpdateLastQuantityChange() throws Exception {
        // Arrange
        Product existingProduct = createExistingProduct();
        existingProduct.setQuantity(1000);
        String uuid = existingProduct.getId().toString();

        ProductValidationModel productValidationModel = createProductValidationModel();
        productValidationModel.setId(uuid);
        productValidationModel.setQuantity(2000);

        when(productService.getProductById(any(UUID.class))).thenReturn(existingProduct);
        when(productService.saveProduct(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            // Check that lastQuantityChange has been updated
            assert savedProduct.getLastQuantityChange().isAfter(existingProduct.getLastQuantityChange());
            return savedProduct;
        });

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productValidationModel)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(2)
    void whenUpdateProductAndQuantityNotChangedShouldNotUpdateLastQuantityChange() throws Exception {
        // Arrange
        Product existingProduct = createExistingProduct();
        existingProduct.setQuantity(1000);
        String uuid = existingProduct.getId().toString();

        ProductValidationModel productValidationModel = createProductValidationModel();
        productValidationModel.setId(uuid);
        productValidationModel.setQuantity(1000);

        when(productService.getProductById(any(UUID.class))).thenReturn(existingProduct);
        when(productService.saveProduct(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            // Check that lastQuantityChange has not been updated
            assert savedProduct.getLastQuantityChange().equals(existingProduct.getLastQuantityChange());
            return savedProduct;
        });

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productValidationModel)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Method for creating an existing product
    private Product createExistingProduct() {
        Product existingProduct = new Product();
        existingProduct.setId(UUID.randomUUID());
        existingProduct.setSku("TEST-SKU");
        existingProduct.setName("Test product");
        existingProduct.setDescription("Test description");
        existingProduct.setCategory("Test Category");
        existingProduct.setPrice(100.00);
        existingProduct.setQuantity(1000);
        existingProduct.setLastQuantityChange(LocalDateTime.now().minusDays(1));
        return existingProduct;
    }

    // Method for creating a product with the received data undergoing validation
    private ProductValidationModel createProductValidationModel() {
        ProductValidationModel productValidationModel = new ProductValidationModel();
        productValidationModel.setId(UUID.randomUUID().toString());
        productValidationModel.setSku("TEST-SKU");
        productValidationModel.setName("Test product");
        productValidationModel.setDescription("Test description");
        productValidationModel.setCategory("Test Category");
        productValidationModel.setPrice(100.00);
        productValidationModel.setQuantity(1000);
        return productValidationModel;
    }

    // Method to convert an object to JSON
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}