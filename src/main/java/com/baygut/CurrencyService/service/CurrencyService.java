package com.baygut.CurrencyService.service;

import com.baygut.CurrencyService.model.Currency;
import com.baygut.CurrencyService.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    /***
     * Gets all of the documents with given arguments
     * @param target filter for target of currency
     * @param source of information
     * @return list of filtered(or not filtered) datas
     */
    public List<Currency> getCurrencies(String target, String source) {
        if(target == null && source == null)
            return currencyRepository.findAll();
        else if(source == null)
            return currencyRepository.findByTarget(target);
        else
            return currencyRepository.findBySource(source);
    }

    /**
     * Saves the given currency to Database
     * @param newCurrency
     * @return
     */
    public Currency createCurrency(Currency newCurrency) {
        return currencyRepository.save(newCurrency);
    }

    /**
     * Deletes the document with given id
     * @param id of document
     */
    public void deleteCurrency(String id) {
        currencyRepository.deleteById(id);
    }


    /**
     * Gets currency by given id
     * @param id of document
     * @return object with given id
     */
    public Currency getCurrencyById(String id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found!")  );
    }
}
