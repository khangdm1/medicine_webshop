package vn.dkk.medicineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dkk.medicineshop.domain.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
