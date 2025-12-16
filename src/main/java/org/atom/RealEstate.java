package org.atom;
public class RealEstate implements Asset {
        private String name;        // 房产名称
        private int cost;           // 购买成本
        private int monthlyReturn;  // 每月租金
        private int currentValue;   // 当前价值

        public RealEstate(String name, int cost, int monthlyReturn) {
            this.name = name;
            this.cost = cost;
            this.monthlyReturn = monthlyReturn;
            this.currentValue = cost; // 初始价值等于购买价
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getCost() {
            return cost;
        }

        @Override
        public int getMonthlyReturn() {
            return monthlyReturn;
        }

        @Override
        public int getCurrentValue() {
            return currentValue;
        }

        // 房产价值波动
        public void fluctuateValue(int percentage) {
            currentValue = currentValue * (100 + percentage) / 100;
        }
    }

