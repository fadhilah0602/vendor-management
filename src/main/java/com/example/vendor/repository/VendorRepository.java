package com.example.vendor.repository;

import com.example.vendor.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByIsDeleteFalse();

    Optional<Vendor> findByName(String name);

    Optional<Vendor> findByIdAndIsDeleteFalse(Long id);
}
