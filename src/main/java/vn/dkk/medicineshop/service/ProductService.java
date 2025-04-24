package vn.dkk.medicineshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.dkk.medicineshop.domain.Product;
import vn.dkk.medicineshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product handleSavePr(Product pr) {
        return this.productRepository.save(pr);
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    public void deleteAProduct(long id) {
        this.productRepository.deleteById(id);
    }
}
