package vn.dkk.medicineshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.dkk.medicineshop.domain.Product;
import vn.dkk.medicineshop.domain.User;
import vn.dkk.medicineshop.domain.dto.RegisterDTO;
import vn.dkk.medicineshop.service.ProductService;
import vn.dkk.medicineshop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;

    public HomePageController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("products", products);
        return "client/homepage";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") RegisterDTO registerDTO) {
        User user = this.userService.registerDTOtoUser(registerDTO);

        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/auth/login";
    }

}
