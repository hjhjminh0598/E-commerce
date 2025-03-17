package com.gnt.ecom.product.service;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.base.BaseServiceImpl;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.product.dto.CreateProductRequest;
import com.gnt.ecom.product.dto.ProductDTO;
import com.gnt.ecom.product.dto.UpdateProductRequest;
import com.gnt.ecom.product.entity.Product;
import com.gnt.ecom.user.service.UserService;
import com.gnt.ecom.user.dto.UserResponse;
import com.gnt.ecom.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, UUID> implements ProductService {

    private final UserService userService;

    protected ProductServiceImpl(BaseRepository<Product, UUID> repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    @Override
    public PageResponse<ProductDTO> getAllProducts(Pageable pageable) {
        return PageResponse.from(super.findAll(pageable).map(ProductDTO::of));
    }

    @Override
    public ProductDTO getById(UUID id) {
        return super.findById(id).map(ProductDTO::of).orElse(null);
    }

    @Override
    public List<ProductDTO> getAllByIds(List<String> ids) {
        List<UUID> uuids = new ArrayList<>();
        for (String id : ids) {
            try {
                uuids.add(UUID.fromString(id));
            } catch (Exception e) {
                continue;
            }
        }

        return uuids.isEmpty() ? List.of() : super.findAllById(uuids).stream().map(ProductDTO::of).toList();
    }

    @Override
    public ProductDTO create(CreateProductRequest request) {
        validateCreateRequest(request);
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        return ProductDTO.of(super.save(product));
    }

    private void validateCreateRequest(CreateProductRequest request) {
        if (StringUtils.isEmpty(request.getName())) {
            throw new RuntimeException("Name is required");
        }
        if (request.getPrice() == null || request.getPrice() < 0) {
            throw new RuntimeException("Price is required");
        }
    }

    @Override
    public ProductDTO update(UUID id, UpdateProductRequest request) {
        Product product = findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        if (StringUtils.isNotEmpty(request.getName())) {
            product.setName(request.getName());
        }
        if (StringUtils.isNotEmpty(request.getDescription())) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null && request.getPrice() >= 0) {
            product.setPrice(request.getPrice());
        }

        return ProductDTO.of(super.save(product));
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userService.getUserById(id);
    }
}
