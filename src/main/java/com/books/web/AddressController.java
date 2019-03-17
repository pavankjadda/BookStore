package com.books.web;

import com.books.model.Address;
import com.books.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController
{
    private final AddressRepository addressRepository;

    @Autowired
    public AddressController(AddressRepository addressRepository)
    {
        this.addressRepository = addressRepository;
    }

    @GetMapping("/address/list")
    public List<Address> getAddressList()
    {
        return addressRepository.findAll();
    }

    @GetMapping("/address/{id}")
    public Address getAddress(@PathVariable Long id)
    {
        return addressRepository.findById(id).orElse(null);
    }

    @GetMapping("/address/{id}/delete")
    public void deleteAddress(@PathVariable Long id)
    {
        addressRepository.deleteById(id);
    }
}
