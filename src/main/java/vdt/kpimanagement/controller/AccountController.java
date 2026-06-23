package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.AccountRequest;
import vdt.kpimanagement.dto.AccountResponse;
import vdt.kpimanagement.entity.Account;
import vdt.kpimanagement.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController extends BaseController<Account, AccountRequest, AccountResponse, Long> {

    public AccountController(AccountService accountService) {
        super(accountService, "tài khoản");
    }
}
