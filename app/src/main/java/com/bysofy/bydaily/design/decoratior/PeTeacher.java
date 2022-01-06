package com.bysofy.bydaily.design.decoratior;
/**
 * @author: 13173
 * @date: 2022/1/6
 * @description:
 */

/**
 * @author BoyuanLiu
 * @date 2022年01月06日 15:34
 */
public class PeTeacher extends Teacher {
    public PeTeacher(Swordsman swordsman) {
        super(swordsman);
    }

    @Override
    public void attackMagic() {
        super.attackMagic();
        peAttackMagic();
    }

    private void peAttackMagic() {
        System.out.println("老师教我体育");
        System.out.println("我学会了体育");
    }
}
