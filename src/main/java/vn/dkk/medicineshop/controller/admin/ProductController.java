package vn.dkk.medicineshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.dkk.medicineshop.domain.Product;
import vn.dkk.medicineshop.service.ProductService;
import vn.dkk.medicineshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    // show pr list
    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> pr = this.productService.getAllProduct();
        model.addAttribute("product", pr);
        return "admin/product/show";
    }

    // create product
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProduct(@ModelAttribute("newProduct") Product pr,
            @RequestParam("imageFile") MultipartFile file) {

        String imgPr = this.uploadService.handleSaveUploadFile(file, "product");
        pr.setImg(imgPr);
        this.productService.handleSavePr(pr);
        return "redirect:/admin/product";
    }

    // get product by id
    @GetMapping("/admin/product/{id}")
    public String getDetailProduct(Model model, @PathVariable long id) {
        Product pr = this.productService.getProductById(id);
        model.addAttribute("id", id);
        model.addAttribute("product", pr);
        return "admin/product/detail";
    }

    // update pr
    @GetMapping("/admin/product/update/{id}")
    public String getUpdatePrPage(Model model, @PathVariable long id) {
        Product currentPr = this.productService.getProductById(id);
        model.addAttribute("currentProduct", currentPr);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(@ModelAttribute("currentProduct") Product pr) {
        Product currentPr = this.productService.getProductById(pr.getId());
        if (currentPr != null) {
            currentPr.setName(pr.getName());
            currentPr.setDescription(pr.getDescription());
            currentPr.setManufacturer(pr.getManufacturer());
            currentPr.setPrice(pr.getPrice());
            currentPr.setCreate_at(pr.getCreate_at());
            this.productService.handleSavePr(currentPr);

        }
        return "redirect:/admin/product";
    }

    // delete product
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        Product pr = this.productService.getProductById(id);
        model.addAttribute("product", pr);

        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postMethodName(@ModelAttribute("product") Product pr) {
        this.productService.deleteAProduct(pr.getId());
        return "redirect:/admin/product";
    }

}
