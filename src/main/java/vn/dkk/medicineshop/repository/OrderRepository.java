package vn.dkk.medicineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dkk.medicineshop.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(long id);
}
