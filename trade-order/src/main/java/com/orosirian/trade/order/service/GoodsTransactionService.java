package com.orosirian.trade.order.service;

public interface GoodsTransactionService {

    boolean tryGoodsStock();

    boolean commitGoodsStock();

    boolean cancelGoodsStock();

}
