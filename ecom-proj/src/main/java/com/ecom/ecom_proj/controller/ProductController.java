package com.ecom.ecom_proj.controller;

import com.ecom.ecom_proj.model.Product;
import com.ecom.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping("/")
    public String greet(){
        return "Hi! Welcome";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){

        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);

    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable int prodId){
        Product product= productService.getProductById(prodId);
        if(product!=null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile image){
        try {
            Product product1 = productService.addProduct(product, image);
            return  new ResponseEntity<>(product1,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product= productService.getProductById(productId);
        byte[] imageFile=product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);

    }

    @PutMapping("product/{prodId}")
    public ResponseEntity<String> updateProduct(@PathVariable int prodId, @RequestPart Product product, @RequestPart MultipartFile image) {
        Product product1 = null;
        try {
            product1 = productService.updateProduct(prodId, product, image);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if (product1 != null) {
            return new ResponseEntity<>("updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodId) {
        Product product = productService.getProductById(prodId);
        if (product != null) {
            productService.deleteProduct(prodId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }



        }

    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        System.out.println("searching with"+ keyword);
        List<Product> products=productService.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);

    }

    }

