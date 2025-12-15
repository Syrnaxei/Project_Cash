package org.atom;

public interface Asset {
        String getName();
        int getCost();
        int getMonthlyReturn(); // 每月被动收入
        int getCurrentValue();  // 当前价值

}
