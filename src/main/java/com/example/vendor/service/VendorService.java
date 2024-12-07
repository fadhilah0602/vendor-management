package com.example.vendor.service;

import com.example.vendor.dto.BaseResponse;
import com.example.vendor.dto.VendorDto;
import com.example.vendor.dto.VendorResponse;
import com.example.vendor.model.Vendor;
import com.example.vendor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public BaseResponse<List<VendorResponse>> getAllVendor () {
        List<Vendor> exist = vendorRepository.findByIsDeleteFalse();
        if(exist.isEmpty()) {
            return BaseResponse.<List<VendorResponse>>builder()
                    .code("204")
                    .status("No Content")
                    .message("Data Not Found")
                    .data(Collections.emptyList())
                    .build();
        }
        List<VendorResponse> vendorResponses = exist.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return BaseResponse.<List<VendorResponse>>builder()
                .code("201")
                .status("ok")
                .message("CREATED SUCCESS")
                .data(vendorResponses)
                .build();
    }

    private VendorResponse mapToResponse(Vendor vendor) {
        return new VendorResponse(
                vendor.getName()
        );
    }

    public BaseResponse<Object> create (VendorDto vendorDto) {
        Optional<Vendor> exist = vendorRepository.findByName(vendorDto.getName());
        if(exist.isPresent()) {
            return BaseResponse.builder()
                    .code("409")
                    .status("Failed")
                    .message("Vendor Already Exist")
                    .data(null)
                    .build();
        }

        Vendor vendor = mapToEntity(vendorDto);
        vendorRepository.save(vendor);

        VendorResponse vendorResponse = mapToResponse(vendor);
        return BaseResponse.builder()
                .code("201")
                .status("ok")
                .message("CREATED SUCCESS")
                .data(vendorResponse)
                .build();
    }

    private Vendor mapToEntity (VendorDto vendorDto) {
        return Vendor.builder()
                .name(vendorDto.getName())
                .build();
    }

    public BaseResponse<Object> update (VendorDto vendorDto, Long id) {
        Optional<Vendor> checkId = vendorRepository.findByIdAndIsDeleteFalse(id);
        Optional<Vendor> exist = vendorRepository.findByName(vendorDto.getName());

        if (checkId.isEmpty()) {
            return BaseResponse.builder()
                    .code("400")
                    .status("Bad Request")
                    .message("Id Not Found")
                    .data(null)
                    .build();
        }
        if(exist.isPresent()) {
            return BaseResponse.builder()
                    .code("409")
                    .status("Failed")
                    .message("Vendor Already Exist")
                    .data(null)
                    .build();
        }

        Vendor vendor = mapToEntity(vendorDto,id);
        vendorRepository.saveAndFlush(vendor);

        VendorResponse vendorResponse = mapToResponse(vendor);
        return BaseResponse.builder()
                .code("200")
                .status("ok")
                .message("UPDATED SUCCESS")
                .data(vendorResponse)
                .build();
    }

    private Vendor mapToEntity (VendorDto vendorDto, Long id) {
        return Vendor.builder()
                .id(id)
                .name(vendorDto.getName())
                .build();
    }

    public BaseResponse<Object> findById (Long id) {
        Optional<Vendor> checkId = vendorRepository.findByIdAndIsDeleteFalse(id);
        if (checkId.isEmpty()) {
            return BaseResponse.builder()
                    .code("400")
                    .status("Bad Request")
                    .message("Id Not Found")
                    .data(null)
                    .build();
        }

        Vendor vendor = checkId.get();
        VendorResponse vendorResponse = mapToResponse(vendor);
        return BaseResponse.builder()
                .code("200")
                .status("ok")
                .message("SUCCESS")
                .data(vendorResponse)
                .build();

    }

    public BaseResponse<Object> softDelete (Long id) {
        Optional<Vendor> checkId = vendorRepository.findByIdAndIsDeleteFalse(id);
        if (checkId.isEmpty()) {
            return BaseResponse.builder()
                    .code("400")
                    .status("Bad Request")
                    .message("Id Not Found")
                    .data(null)
                    .build();
        }

        Vendor vendor = checkId.get();
        vendor.setDelete(true);
        vendorRepository.saveAndFlush(vendor);
        VendorResponse vendorResponse = mapToResponse(vendor);
        return BaseResponse.builder()
                .code("200")
                .status("ok")
                .message("DELETED SUCCESS")
                .data(vendorResponse)
                .build();

    }
}
