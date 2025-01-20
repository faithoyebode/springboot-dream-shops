package com.faithoyebode.dreamshops.service.product;

import com.faithoyebode.dreamshops.exception.ProductNotFoundException;
import com.faithoyebode.dreamshops.model.Category;
import com.faithoyebode.dreamshops.model.Product;
import com.faithoyebode.dreamshops.repository.CategoryRepository;
import com.faithoyebode.dreamshops.repository.ProductRepository;
import com.faithoyebode.dreamshops.request.AddProductRequest;
import com.faithoyebode.dreamshops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        //check if the category is found in the DB
        //If Yes, set it as the product category
        //If No, save it as a new category in the DB
        //Then set it as the product category
        List<Category> category = categoryRepository.findByName(request.getCategory().getName());
        if (category.isEmpty()) {
            Category newCategory = new Category(request.getCategory().getName());
            Category savedCategory = categoryRepository.save(newCategory);
            category = List.of(savedCategory);
        }
        request.setCategory(category.get(0));
        return productRepository.save(createProduct(request, category.get(0)));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
            request.getName(),
            request.getBrand(),
            request.getPrice(),
            request.getInventory(),
            request.getDescription(),
            category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                ()->{throw new ProductNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
    return productRepository.findById(productId)
            .map(existingProduct -> updateExistingProduct(existingProduct, request))
            .map(productRepository::save)
            .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setName(request.getName());

        List<Category> category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category.get(0));
        return existingProduct;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
