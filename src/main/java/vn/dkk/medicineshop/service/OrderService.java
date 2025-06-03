package vn.dkk.medicineshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.dkk.medicineshop.domain.Order;
import vn.dkk.medicineshop.domain.OrderDetail;
import vn.dkk.medicineshop.repository.OrderDetailRepository;
import vn.dkk.medicineshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Order> getAllOrder() {
        return this.orderRepository.findAll();
    }

    public Order getOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public void deleteAOrder(long id) {
        // delete order detail
        Order order = this.getOrderById(id);
        if (order != null) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
        }
        this.orderRepository.deleteById(id);
    }
}
