package com.baygut.CurrencyService.schedule;


import com.baygut.CurrencyService.controller.CurrencyController;
import com.baygut.CurrencyService.model.Currency;
import com.baygut.CurrencyService.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/***
 * Data Receiving Automation. This method runs every hour. Receives data from 2 different apis and saves the datas to database.
 */
@AllArgsConstructor
@Service
public class Scheduler {

    private final CurrencyService currencyService;

    @Scheduled(fixedDelay = 3_600_600)
    public void collectDataAutomation() {
        String sourceUSD_1 = "https://api.apilayer.com/exchangerates_data/convert?to=TRY&from=USD&amount=1";
        String sourceUSD_2 = "https://api.apilayer.com/currency_data/convert?to=TRY&from=USD&amount=1";
        String sourceEUR_1 = "https://api.apilayer.com/exchangerates_data/convert?to=TRY&from=EUR&amount=1";
        String sourceEUR_2 = "https://api.apilayer.com/currency_data/convert?to=TRY&from=EUR&amount=1";
        var c1 = collectDataFromSources(sourceUSD_1, "USD");
        var c2 = collectDataFromSources(sourceUSD_2, "USD");
        var c3 = collectDataFromSources(sourceEUR_1, "EUR");
        var c4 = collectDataFromSources(sourceEUR_2, "EUR");

        currencyService.createCurrency(c1);
        currencyService.createCurrency(c2);
        currencyService.createCurrency(c3);
        currencyService.createCurrency(c4);
        System.out.println("Data received...");
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
