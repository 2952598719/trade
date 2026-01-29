package com.orosirian.trade.goods.service.impl;

import com.orosirian.trade.goods.service.GoodsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsTransactionServiceImpl implements GoodsTransactionService {

    // 空置，要完善这个项目时再去完成

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
