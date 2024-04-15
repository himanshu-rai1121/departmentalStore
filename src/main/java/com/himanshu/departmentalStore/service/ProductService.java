package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product){
        product.setId(id);
        return productRepository.save(product);
    }
//
//    public CompletableFuture<Boolean> deleteProduct(Long id) {
//        return CompletableFuture.supplyAsync(() -> {
//            Optional<Product> optionalProduct = productRepository.findById(id);
//            if (optionalProduct.isPresent()) {
//                productRepository.deleteById(id);
//                return true;
//            } else {
//                return false;
//            }
//        });
//    }

    public Boolean deleteProduct(Long id) {

            Optional<Product> optionalProduct = productRepository.findById(id);
            System.out.println((optionalProduct));
//        return true;
            if (optionalProduct.isPresent()) {
                productRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        };
    }



