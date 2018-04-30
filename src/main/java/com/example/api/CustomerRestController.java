	package com.example.api;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.domain.Customer;
import com.example.service.CustomerService;

@RestController
@RequestMapping("api//customers")
public class CustomerRestController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    Page<Customer> getCustomers(@PageableDefault Pageable pageable) {
    	Page<Customer> customers = customerService.findAll(pageable);
    	return customers;
    }


    @GetMapping(path = "{id}")
    Customer getCustomer(@PathVariable Integer id) {
        Customer customer = customerService.findOne(id);
        return customer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Customer postCustomers(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping(path = "{id}")
    Customer putCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        customer.setId(id);
        return customerService.update(customer);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus
    void deleteCusotmer(@PathVariable Integer id) {
        customerService.delete(id);
    }

    @PostMapping
    ResponseEntity<Customer> postCustomers(@RequestBody Customer customer,
            UriComponentsBuilder uriBuilder) {
        Customer created = customerService.create(customer);
        URI location = uriBuilder.path("api/Customers/{id}")
                .buildAndExpand(created.getId()).toUri();
                return ResponseEntity.created(location).body(created);
    }

}
