package com.bysofy.bydaily.design.decoratior;
/**
 * @author: 13173
 * @date: 2022/1/6
 * @description: 具体要拓展的功能
 */

/**
 * @author BoyuanLiu
 * @date 2022年01月06日 15:29
 */
public class MathTeacher extends Teacher {
    public MathTeacher(Swordsman swordsman) {
        super(swordsman);
    }

    private void mathAttackMagic() {
        System.out.println("老师教我数学");
        System.out.println("我学会了数学");
    }

    @Override
    public void attackMagic() {
        super.attackMagic();
        mathAttackMagic();
    }
}
