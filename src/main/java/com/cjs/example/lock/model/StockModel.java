package com.cjs.example.lock.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ChengJianSheng
 * @date 2019-07-30
 */
@Data
public class StockModel implements Serializable {

    private Integer id;

    private Integer productId;

    private Integer stock;

}
