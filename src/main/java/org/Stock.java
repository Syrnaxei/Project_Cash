package org;

public class Stock implements Asset {
    private String name;        // 股票名称
    private int cost;           // 购买成本
    private int shares;         // 持股数量
    private int currentPrice;   // 当前单价

    public Stock(String name, int costPerShare, int shares) {
        this.name = name;
        this.cost = costPerShare * shares;//每股购买成本
        this.shares = shares;
        this.currentPrice = costPerShare;
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
        // 股票股息可以按持股数量计算，这里简单返回固定比例
        return (currentPrice * shares) / 200; // 假设 0.5% 股息率
    }

    @Override
    public int getCurrentValue() {
        return currentPrice * shares;
    }

    // 股价波动
    public void fluctuatePrice(int percentage) {
        currentPrice = currentPrice * (100 + percentage) / 100;
    }
}