package com.baygut.CurrencyService.controller;
import com.baygut.CurrencyService.model.Currency;
import com.baygut.CurrencyService.schedule.Scheduler;
import com.baygut.CurrencyService.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/currencies")
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;


    /**
     * Runs when GET request sent to browser or POSTMAN.
     * @param target of currency
     * @param source of the info
     * @return list of requested data and HttpStatus.OK
     */
    @GetMapping
    public ResponseEntity<List<Currency>> getCurrencies(@RequestParam(required = false) String target, @RequestParam(required = false) String source) {
        return new ResponseEntity<>(currencyService.getCurrencies(target, source), OK);
    }

    /**
     * Runs when GET request sent to browser or POSTMAN,
     * @param id of document
     * @return requested document and HttpStatus.OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String id) {
        return new ResponseEntity<>(getCurrencyById(id), OK);
    }

    /**
     * Runs when POST request sent to browser or POSTMAN. It posts the data to Database.
     * @param newCurrency the data that wanted to post to database.
     * @return created object and HttpStatus.CREATED
     */
    @PostMapping
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency newCurrency) {
        return new ResponseEntity<>(currencyService.createCurrency(newCurrency), CREATED);
    }

    /***
     * Deletes the given document
     * @param id of document
     * @return HttpStatus.OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String id) {
        currencyService.deleteCurrency(id);
        return new ResponseEntity<>(OK);
    }


    private Currency getCurrencyById(String id) {
        return currencyService.getCurrencyById(id);
    }

}
