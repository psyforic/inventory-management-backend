package productinventory_backend.productinventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productinventory_backend.productinventory.exception.ResourceNotFoundException;
import productinventory_backend.productinventory.model.ApiResponse;
import productinventory_backend.productinventory.model.Product;
import productinventory_backend.productinventory.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final ProductRepository productRepository;
    private static Product updateProduct;
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/products")
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product)
    {
        updateProduct=new Product(product.getProductName(), product.getProductCategory(), product.getDate(),
                product.getPrice(), product.getProductCondition(), product.getComment(), product.getDescription(),product.getRemaining(), product.getTotal());
        return productRepository.save(updateProduct);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id)
    {
        Product product =productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product id :"+id+" does not exist"));
        return ResponseEntity.ok(product);
    }

     @CrossOrigin(origins = "http://localhost:4200")
     @DeleteMapping("/products/{id}")
     public ResponseEntity<Map<String,Boolean>> deleteProduct(@PathVariable("id") Long id)
     {
         Product product =productRepository.findById(id)
                 .orElseThrow(()->new ResourceNotFoundException("Product id :"+id+" does not exist"));
         productRepository.delete(product);
         Map<String,Boolean> response=new HashMap<>();
         response.put("deleted",Boolean.TRUE);
         return ResponseEntity.ok(response);
     }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Long id,@RequestBody @Valid Product product)
    {

        updateProduct =productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product id :"+id+" does not exist"));
       updateProduct.setProductName(product.getProductName());
       updateProduct.setPrice(product.getPrice());
       updateProduct.setProductCategory(product.getProductCategory());
       updateProduct.setDescription(product.getDescription());
       updateProduct.setComment(product.getComment());
       updateProduct.setProductCondition(product.getProductCondition());
       updateProduct.setDate(product.getDate());
        Product updatedProduct=productRepository.save(updateProduct);
        return ResponseEntity.ok(updatedProduct);

    }
    
}
