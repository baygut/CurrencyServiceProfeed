package com.baygut.CurrencyService.controller;
import com.baygut.CurrencyService.model.Currency;
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

    /***
     * Data Receiving Automation. This method runs every hour. Receives data from 2 different apis and saves the datas to database.
     */
    @Scheduled(fixedDelay = 3_600_000)
    public void collectDataAutomation() {
        String sourceUSD_1 = "https://api.apilayer.com/exchangerates_data/convert?to=TRY&from=USD&amount=1";
        String sourceUSD_2 = "https://api.apilayer.com/currency_data/convert?to=TRY&from=USD&amount=1";
        String sourceEUR_1 = "https://api.apilayer.com/exchangerates_data/convert?to=TRY&from=EUR&amount=1";
        String sourceEUR_2 = "https://api.apilayer.com/currency_data/convert?to=TRY&from=EUR&amount=1";
        var c1 = collectDataFromSources(sourceUSD_1, "USD");
        var c2 = collectDataFromSources(sourceUSD_2, "USD");
        var c3 = collectDataFromSources(sourceEUR_1, "EUR");
        var c4 = collectDataFromSources(sourceEUR_2, "EUR");
        createCurrency(c1);
        createCurrency(c2);
        createCurrency(c3);
        createCurrency(c4);
        System.out.println("Data received...");
    }

    private Currency getCurrencyById(String id) {
        return currencyService.getCurrencyById(id);
    }

    private Currency collectDataFromSources(String source, String target) {
        Currency currency = new Currency();

        HttpHeaders headers = new HttpHeaders();
        headers.add("apikey", "BrGPVIMYsIhvQhYxbVgoNp5qqQ84DySI");
        HttpEntity<Object> entity=new HttpEntity<Object>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(source, HttpMethod.GET, entity, String.class);

        final JSONObject obj = new JSONObject(response.getBody());

        currency.setSource(source);
        currency.setTarget(target);
        currency.setSellPrice(obj.getDouble("result"));
        currency.setBuyPrice(obj.getDouble("result"));

        return currency;
    }
}
