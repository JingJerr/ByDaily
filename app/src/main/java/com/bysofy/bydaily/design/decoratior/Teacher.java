package com.bysofy.bydaily.design.decoratior;
/**
 * @author: 13173
 * @date: 2022/1/6
 * @description:
 */

/**
 * @author BoyuanLiu
 * @date 2022年01月06日 15:30
 */
public class Teacher extends Swordsman {
    //持有基础被装饰着的引用
    private Swordsman swordsman;

    public Teacher(Swordsman swordsman) {
        this.swordsman = swordsman;
    }

    @Override
    public void attackMagic() {
        swordsman.attackMagic();
    }
}
