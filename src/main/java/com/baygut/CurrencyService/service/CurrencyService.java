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

    public List<Currency> getCurrencies(String target) {
        if(target == null)
            return currencyRepository.findAll();
        else
            return currencyRepository.findByTarget(target);
    }

    public Currency createCurrency(Currency newCurrency) {
        return currencyRepository.save(newCurrency);
    }

    public void deleteCurrency(String id) {
        currencyRepository.deleteById(id);
    }

    public Currency getCurrencyById(String id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found!")  );
    }
}
