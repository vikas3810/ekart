package com.ekart.serviceImpl;

import com.ekart.dto.request.ProductDto;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.model.Category;
import com.ekart.model.Product;
import com.ekart.model.User;
import com.ekart.repo.CategoryRepo;
import com.ekart.repo.ProductRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    /**
     * Adds a new product.
     *
     * @param productDto The DTO containing product information.
     * @param emailId    The email ID of the user.
     * @return The ID of the newly added product.
     * @throws EntityAlreadyExistException If the product already exists.
     */
    @Override
    public int addProduct(@Valid ProductDto productDto, String emailId) throws EntityAlreadyExistException {
        log.info("Inside addProduct method");

        // Retrieve the user entity
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(ResourceNotFoundException::new);

        // Retrieve the category entity
        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(ResourceNotFoundException::new);

        // Create a new product
        Product product = Product.builder()
                .productName(productDto.getProductName())
                .serialNumber(productDto.getSerialNumber())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .slug(productDto.getSlug())
                .category(category)
                .description(productDto.getDescription())
                .isActive(true)
                .createdBy(user.getEmailId())
                .createdDate(LocalDate.now())
                .modifiedBy(user.getEmailId())
                .modifiedDate(LocalDate.now())
                .build();

        // Save the product
        return productRepo.save(product).getProductId();
    }

    /**
     * Deletes a product by setting it as inactive.
     *
     * @param productId The ID of the product to delete.
     * @return A message indicating that the product was deleted.
     */
    @Override
    public String delete(int productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);
        product.setActive(false);
        productRepo.save(product);
        return "Product deleted";
    }

    /**
     * Updates an existing product.
     *
     * @param productId  The ID of the product to update.
     * @param emailId    The email ID of the user performing the update.
     * @param productDto The DTO containing updated product information.
     * @return A message indicating the successful update of the product.
     */
    @Override
    public String update(int productId, String emailId, ProductDto productDto) {
        Product product = productRepo.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);

        // Update product information
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setModifiedBy(emailId);
        product.setModifiedDate(LocalDate.now());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setSerialNumber(productDto.getSerialNumber());
        product.setSlug(productDto.getSlug());

        // Save the updated product
        productRepo.save(product);
        return "Product updated successfully";
    }

    /**
     * Retrieves a list of all active products.
     *
     * @return A list of active products.
     */
    @Override
    public List<Product> dispAllProduct() {
        return productRepo.findAllByAndIsActive(true);
    }
}
