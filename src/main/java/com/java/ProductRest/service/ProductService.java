package com.java.ProductRest.service;

import com.java.ProductRest.dto.ProductRequestDTO;
import com.java.ProductRest.dto.ProductResponseDTO;
import com.java.ProductRest.entity.Product;
import com.java.ProductRest.exception.ProductNotFoundException;
import com.java.ProductRest.exception.ProductServiceBusinessException;
import com.java.ProductRest.repository.ProductRepository;
import com.java.ProductRest.util.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO createNewProduct(ProductRequestDTO productRequestDTO) throws ProductServiceBusinessException {
        ProductResponseDTO productResponseDTO;
        try {
            log.info("ProductService:createNewProduct execution started.");
            Product product = ValueMapper.convertToEntity(productRequestDTO);
            log.debug("ProductService:createNewProduct request parameters {}", ValueMapper.jsonAsString(productRequestDTO));
            Product productResults = productRepository.save(product);
            productResponseDTO = ValueMapper.convertToDTO(productResults);
            log.debug("ProductService:createNewProduct received response from Database {}", ValueMapper.jsonAsString(productRequestDTO));
        } catch (Exception ex) {
            log.error("Exception occurred while persisting product to database , Exception message {}", ex.getMessage());
            throw new ProductServiceBusinessException("Exception occurred while create a new product");
        }
        log.info("ProductService:createNewProduct execution ended.");
        return productResponseDTO;
    }

    public List<ProductResponseDTO> getProducts() throws ProductServiceBusinessException {
        List<ProductResponseDTO> productResponseDTOS = null;
        try {
            log.info("ProductService:getProducts execution started.");
            List<Product> productList = productRepository.findAll();
            if (!productList.isEmpty()) {
                productResponseDTOS = productList.stream()
                        .map(ValueMapper::convertToDTO)
                        .collect(Collectors.toList());
            } else {
                productResponseDTOS = Collections.emptyList();
            }
            log.debug("ProductService:getProducts retrieving products from database  {}", ValueMapper.jsonAsString(productResponseDTOS));
        } catch (Exception ex) {
            log.error("Exception occurred while retrieving products from database , Exception message {}", ex.getMessage());
            throw new ProductServiceBusinessException("Exception occurred while fetch all products from Database");
        }
        log.info("ProductService:getProducts execution ended.");
        return productResponseDTOS;
    }

    public ProductResponseDTO getProductById(long productId) {
        ProductResponseDTO productResponseDTO;
        try {
            log.info("ProductService:getProductById execution started.");
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + productId));
            productResponseDTO = ValueMapper.convertToDTO(product);
            log.debug("ProductService:getProductById retrieving product from database for id {} {}", productId, ValueMapper.jsonAsString(productResponseDTO));
        } catch (Exception ex) {
            log.error("Exception occurred while retrieving product {} from database , Exception message {}", productId, ex.getMessage());
            throw new ProductServiceBusinessException("Exception occurred while fetch product from Database " + productId);
        }
        log.info("ProductService:getProductById execution ended.");
        return productResponseDTO;
    }

    public Map<String, List<ProductResponseDTO>> getProductsByTypes() {
        try {
            log.info("ProductService:getProductsByTypes execution started.");
            Map<String, List<ProductResponseDTO>> productsMap =
                    productRepository.findAll().stream()
                            .map(ValueMapper::convertToDTO)
                            .filter(productResponseDTO -> productResponseDTO.getProductType() != null)
                            .collect(Collectors.groupingBy(ProductResponseDTO::getProductType));
            log.info("ProductService:getProductsByTypes execution ended.");
            return productsMap;
        } catch (Exception ex) {
            log.error("Exception occurred while retrieving product grouping by type from database , Exception message {}", ex.getMessage());
            throw new ProductServiceBusinessException("Exception occurred while fetch product from Database ");
        }
    }
}
