package com.example.vendor.controller;

import com.example.vendor.dto.BaseResponse;
import com.example.vendor.dto.VendorDto;
import com.example.vendor.dto.VendorResponse;
import com.example.vendor.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    VendorService vendorService;
    @GetMapping(value = "/getAll")
    public BaseResponse<List<VendorResponse>> getAll () {
        BaseResponse<List<VendorResponse>> vendorResponses = vendorService.getAllVendor();

        List<VendorResponse> data = vendorResponses.getData() != null
                ? vendorResponses.getData()
                : Collections.emptyList();
        if(data.isEmpty()) {
            return BaseResponse.<List<VendorResponse>>builder()
                    .code("204")
                    .status("No Content")
                    .message("Data Not Found")
                    .data(Collections.emptyList())
                    .build();
        } else {
            return BaseResponse.<List<VendorResponse>>builder()
                    .code("200")
                    .status("Ok")
                    .message("Data is Present")
                    .data(data.stream().toList())
                    .build();
        }

    }

    @PostMapping(value = "/create")
    public BaseResponse<Object> create (@RequestBody VendorDto vendorDto) {
        return vendorService.create(vendorDto);
    }

    @PutMapping(value = "/update/{id}")
    public BaseResponse<Object> update (@RequestBody VendorDto vendorDto, @PathVariable Long id) {
        return vendorService.update(vendorDto, id);
    }

    @GetMapping(value = "/findById/{id}")
    public BaseResponse<Object> update (@PathVariable Long id) {
        return vendorService.findById(id);
    }

    @DeleteMapping(value = "/softDelete/{id}")
    public BaseResponse<Object> softDelete (@PathVariable Long id) {
        return vendorService.softDelete(id);
    }



}
