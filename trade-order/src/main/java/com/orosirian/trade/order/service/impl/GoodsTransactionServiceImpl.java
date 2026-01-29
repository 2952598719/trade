package com.orosirian.trade.order.service.impl;

import com.orosirian.trade.order.service.GoodsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsTransactionServiceImpl implements GoodsTransactionService {

    // 空置

    @Override
    public boolean tryGoodsStock() {
        log.info("tryGoodsStock......");
        return true;
    }

    @Override
    public boolean commitGoodsStock() {
        log.info("commitGoodsStock......");
        return true;
    }

    @Override
    public boolean cancelGoodsStock() {
        log.info("cancelGoodsStock......");
        return true;
    }

}
