package com.furkantokgz.productapi.Controller;

import com.furkantokgz.productapi.Dto.ProductRequest;
import com.furkantokgz.productapi.Dto.ProductResponse;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/apiv1")
public class ProductController {

    private final Map<String, ProductResponse> productResponseMap= new HashMap<>(); //Local database

    @GetMapping("/{id}")
    public EntityModel<ProductResponse> get(@PathVariable String id) {
        ProductResponse productResponse = productResponseMap.get(id);
        if (productResponse == null) {
            throw new NoSuchElementException("Product not found");
        }
        //return EntityModel.of(productResponse);
        return toModel(productResponse);
    }

    @GetMapping("/productlist")
    public CollectionModel<EntityModel<ProductResponse>> getAll() {
        List<EntityModel<ProductResponse>> productList = productResponseMap.values().stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(productList, linkTo(methodOn(ProductController.class).getAll()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProductResponse>> create(@RequestBody ProductRequest productRequest) {
        String id = UUID.randomUUID().toString();
        ProductResponse response = new ProductResponse(id,productRequest.name().toString(), productRequest.price());
        productResponseMap.put(id,response);
        EntityModel<ProductResponse> entityModel = toModel(response);
        return ResponseEntity.created(linkTo(methodOn(ProductController.class).get(id)).toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public EntityModel<ProductResponse> update(@PathVariable String id, ProductRequest productRequest) {
        if(!productResponseMap.containsKey(id)) {
            throw new NoSuchElementException("Product not found");
        }
        ProductResponse response = new ProductResponse(id,productRequest.name().toString(), productRequest.price());
        productResponseMap.put(id,response);
        return toModel(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if(!productResponseMap.containsKey(id)) {
            throw new NoSuchElementException("Product not found");
        }
        productResponseMap.remove(id);
        return ResponseEntity.noContent().build();
    }

    public EntityModel<ProductResponse> toModel(ProductResponse productResponse) {
        return EntityModel.of(productResponse,
                linkTo(methodOn(ProductController.class).getAll()).withSelfRel());
    }
}
