package com.orosirian.trade.goods.service;

public interface GoodsTransactionService {

    boolean tryGoodsStock();

    boolean commitGoodsStock();

    boolean cancelGoodsStock();

}
