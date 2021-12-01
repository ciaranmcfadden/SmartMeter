package com.ciaran.smartmeter.controller;

import com.ciaran.smartmeter.model.AccountDetails;
import com.ciaran.smartmeter.service.SmartMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/smart")
public class SmartMeterController {

    private SmartMeterService smartMeterService;

    @Autowired
    public SmartMeterController(SmartMeterService smartMeterService) {
        this.smartMeterService = smartMeterService;
    }

    @GetMapping(value = "/reads/{accountNumber}", produces = "application/json")
    public ResponseEntity<AccountDetails> getSmartMeterAccountByAccountId(@PathVariable long accountNumber) {
        AccountDetails accountDetails = smartMeterService.getAccountDetailsByAccountId(accountNumber);
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);
    }

    @PostMapping(value="/reads", produces = "application/json")
    public ResponseEntity<AccountDetails> addNewSmartMeterReadings(@RequestBody AccountDetails accountDetails) {
        AccountDetails newAccountDetails = smartMeterService.addNewSmartMeterReadings(accountDetails);
        return new ResponseEntity<>(newAccountDetails, HttpStatus.CREATED);
    }

}
