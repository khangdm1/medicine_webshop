package vn.dkk.medicineshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import vn.dkk.medicineshop.domain.Order;
import vn.dkk.medicineshop.domain.OrderDetail;
import vn.dkk.medicineshop.domain.Product;
import vn.dkk.medicineshop.repository.OrderDetailRepository;
import vn.dkk.medicineshop.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailRepository orderDetailRepository;

    public OrderController(OrderService orderService,
            OrderDetailRepository orderDetailRepository) {
        this.orderService = orderService;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Model model) {
        List<Order> order = this.orderService.getAllOrder();
        model.addAttribute("orders", order);
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getDetailOrder(Model model, @PathVariable long id) {
        Order order = this.orderService.getOrderById(id);
        List<OrderDetail> orderDetails = this.orderDetailRepository.findAll();
        model.addAttribute("orderDetails", orderDetails);
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        return "admin/order/update";
    }

    // delete order
    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable long id) {
        Order order = this.orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteOrder(@ModelAttribute("order") Order order) {
        this.orderService.deleteAOrder(order.getId());
        return "redirect:/admin/order";
    }

}
