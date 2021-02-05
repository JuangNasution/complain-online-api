package id.co.bni.ets.complain_online.controller;

import id.co.bni.ets.complain_online.model.CustomerCard;
import id.co.bni.ets.complain_online.service.CustomerService;
import id.co.bni.ets.lib.model.ApiResponse;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Juang Nasution
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ApiResponse<?> resgistCustomer(@Valid @RequestBody CustomerCard customerCard, BindingResult bindingResult) {
        return ApiResponse.apiOk(customerService.registCustomer(customerCard));
    }

    @PostMapping("/login")
    public ApiResponse<?> loginCustomer(@Valid @RequestBody String email, String password) {
        return ApiResponse.apiOk(customerService.loginCustomer(email, password));
    }

}
