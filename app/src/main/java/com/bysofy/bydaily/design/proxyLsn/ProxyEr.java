package com.bysofy.bydaily.design.proxyLsn;
/**
 * @author: 13173
 * @date: 2022/1/5
 * @description:
 */

/**
 * @author BoyuanLiu
 * @date 2022年01月05日 15:14
 */
public class ProxyEr implements IShop {
    private IShop mShop;

    public ProxyEr(IShop mShop) {
        this.mShop = mShop;
    }

    @Override
    public void buyHouse() {
        mShop.buyHouse();
    }
}
