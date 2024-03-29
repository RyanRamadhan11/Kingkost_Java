package com.enigma.kingkost.controllers;

import com.enigma.kingkost.constant.AppPath;
import com.enigma.kingkost.dto.request.CustomerRequest;
import com.enigma.kingkost.dto.response.CommondResponse;
import com.enigma.kingkost.dto.response.CustomerResponse;
import com.enigma.kingkost.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = AppPath.URL_CROSS)
@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.CUSTOMER)
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/v1")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<CustomerResponse> getAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/v1/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public CustomerResponse getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    @DeleteMapping("/v1/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public void deleteCust(@PathVariable String id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("/v1")
    @CrossOrigin(origins = "http://localhost:5173")
    public CustomerResponse updateCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.update(customerRequest);
    }


    @PostMapping(value = "/v1/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<CustomerResponse> uploadCustomerImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            CustomerResponse customer = customerService.addOrUpdateProfileImageForCustomer(id, file);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<CommondResponse> getCustomerByUserCredentialId(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getCustomerByUserCredentialId(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommondResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get customer")
                .data(customerResponse)
                .build());
    }
}
