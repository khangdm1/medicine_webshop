package vn.dkk.medicineshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import vn.dkk.medicineshop.domain.Cart;
import vn.dkk.medicineshop.domain.CartDetail;
import vn.dkk.medicineshop.domain.Order;
import vn.dkk.medicineshop.domain.OrderDetail;
import vn.dkk.medicineshop.domain.Product;
import vn.dkk.medicineshop.domain.User;
import vn.dkk.medicineshop.repository.CartDetailRepository;
import vn.dkk.medicineshop.repository.CartRepository;
import vn.dkk.medicineshop.repository.OrderDetailRepository;
import vn.dkk.medicineshop.repository.OrderRepository;
import vn.dkk.medicineshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository,
            UserService userService,
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository,
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
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

    public List<CartDetail> getAllCartDetail() {
        return this.cartDetailRepository.findAll();
    }

    public Cart getCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);
                cart = this.cartRepository.save(newCart);
            }
            // save cart_detail
            // find product
            Product product = this.getProductById(productId);
            boolean isExistProductInCart = this.cartDetailRepository.existsByCartAndProduct(cart, product);
            if (!isExistProductInCart) {
                // tạo mới và gán giá trị cho cartdetail
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(1);
                this.cartDetailRepository.save(cartDetail);

                // update cart (sum)
                int sum = cart.getSum() + 1;
                cart.setSum(sum);
                this.cartRepository.save(cart);
                session.setAttribute("sum", sum);
            } else {
                CartDetail old = this.cartDetailRepository.findByCartAndProduct(cart, product);
                old.setQuantity(old.getQuantity() + 1);
                this.cartDetailRepository.save(old);
            }
        }
    }

    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetaiOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetaiOptional.isPresent()) {
            CartDetail cartDetail = cartDetaiOptional.get();
            Cart currentCart = cartDetail.getCart();
            this.cartDetailRepository.deleteById(cartDetailId);
            // update cart
            if (currentCart.getSum() > 1) {
                int sum = currentCart.getSum() - 1;
                currentCart.setSum(sum);
                session.setAttribute("sum", sum);
                this.cartRepository.save(currentCart);
            } else {
                // this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
                currentCart.setSum(0);
                this.cartRepository.save(currentCart);
            }
        }
    }

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                CartDetail currentCartDetail = cdOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    public void handlePlaceOrder(User user,
            HttpSession session,
            String receiverName,
            String receiverAddress,
            String receiverPhone) {
        // create orderdetail
        // get cart by user
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null) {
                // create order
                Order order = new Order();
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAddress);
                order.setReceiverPhone(receiverPhone);
                order.setStatus("PENDING");
                double sum = 0;
                for (CartDetail cd : cartDetails) {
                    sum += cd.getPrice();
                }
                order.setTotalPrice(sum);
                order = this.orderRepository.save(order);
                // order detail
                for (CartDetail cd : cartDetails) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());
                    this.orderDetailRepository.save(orderDetail);
                }
                // delete cart detail
                for (CartDetail cd : cartDetails) {
                    this.cartDetailRepository.deleteById(cd.getId());
                }
            }
            cart.getCartDetails().clear();
            cart.setSum(0);
            this.cartRepository.save(cart);
            session.setAttribute("sum", 0);

        }
    }

    public List<Product> searchProductsForChatbot(String keyword) {
        return this.productRepository.searchByKeyword(keyword);
    }

    public String getShopInfo() {
        return "Tên cửa hàng của chúng ta là Nhà thuốc DKK, chuyên cung cấp các sản phẩm y tế và dược phẩm chất lượng cao.";
    }

    public String listAvailableProductsSummary() {
        List<Product> products = this.productRepository.findAll();
        long totalProducts = products.size();

        if (totalProducts == 0) {
            return "Hiện tại cửa hàng chưa có sản phẩm nào.";
        }

        // Chỉ liệt kê 5 tên sản phẩm đầu tiên để tóm tắt
        String summaryList = products.stream().limit(5)
                .map(Product::getName)
                .collect(Collectors.joining(", "));

        return String.format(
                "Cửa hàng hiện đang có tổng cộng %d loại sản phẩm. Một vài sản phẩm tiêu biểu là: %s...",
                totalProducts,
                summaryList);
    }

    public String getProductDetails(String productName) {
        // Tận dụng lại hàm tìm kiếm đã có
        List<Product> products = productRepository.searchByKeyword(productName);

        if (products.isEmpty()) {
            return "Xin lỗi, tôi không tìm thấy sản phẩm nào có tên gần giống '" + productName + "' trong cửa hàng.";
        }

        // Lấy sản phẩm đầu tiên tìm được để hiển thị chi tiết
        Product p = products.get(0);
        return String.format(
                "Thông tin chi tiết về sản phẩm '%s': [Mô tả: %s], [Giá bán: %,.0fđ].",
                p.getName(),
                p.getDescription(),
                p.getPrice());
    }

    public List<Product> filterSuggestedProducts(List<String> suggestedNames) {
        if (suggestedNames == null || suggestedNames.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return this.productRepository.findByNameIn(suggestedNames);
    }
}
