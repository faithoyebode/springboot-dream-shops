package com.faithoyebode.dreamshops.controller;

import com.faithoyebode.dreamshops.exception.ResourceNotFoundException;
import com.faithoyebode.dreamshops.model.Product;
import com.faithoyebode.dreamshops.request.AddProductRequest;
import com.faithoyebode.dreamshops.request.ProductUpdateRequest;
import com.faithoyebode.dreamshops.response.ApiResponse;
import com.faithoyebode.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(name = "name", required = false) String productName,
            @RequestParam(name = "brand", required = false) String brand,
            @RequestParam(name = "category", required = false) String category
            ){
        List<Product> products;

        if(brand != null && productName != null){
            try {
                products = productService.getProductsByBrandAndName(brand, productName);
                if(products.isEmpty()){
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found", null));
                }
                return ResponseEntity.ok(new ApiResponse("Success!", products));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        if(category != null && brand != null){
            try {
                products = productService.getProductsByCategoryAndBrand(category, brand);
                if(products.isEmpty()){
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found", null));
                }
                return ResponseEntity.ok(new ApiResponse("Success!", products));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        if(productName != null){
            try {
                products = productService.getProductsByName(productName);
                if(products.isEmpty()){
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found", null));
                }
                return ResponseEntity.ok(new ApiResponse("Success!", products));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        if(brand != null){
            try {
                products = productService.getProductsByBrand(brand);
                if(products.isEmpty()){
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found", null));
                }
                return ResponseEntity.ok(new ApiResponse("Success!", products));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        if(category != null ){
            try {
                products = productService.getProductsByCategory(category);
                if(products.isEmpty()){
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found", null));
                }
                return ResponseEntity.ok(new ApiResponse("Success!", products));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Success!", products));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product theProduct = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Success!", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product theProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add product success!", theProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product, @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(product, productId);
            return ResponseEntity.ok(new ApiResponse("Add product success!", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product success!", productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Success!", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
