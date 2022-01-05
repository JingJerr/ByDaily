package com.bysofy.bydaily.design.proxyLsn;
/**
 * @author: 13173
 * @date: 2022/1/5
 * @description:真实类（被代理者）
 */

/**
 * @author BoyuanLiu
 * @date 2022年01月05日 15:12
 */
public class ByRelSub implements IShop {
    @Override
    public void buyHouse() {
        System.out.println("buy House");
    }
}
