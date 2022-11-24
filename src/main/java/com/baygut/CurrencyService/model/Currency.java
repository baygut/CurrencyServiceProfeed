package com.baygut.CurrencyService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    private String id;
    private String source;
    private Date time = new Date();
    private String target;
    private Double buyPrice;
    private Double sellPrice;

}
















