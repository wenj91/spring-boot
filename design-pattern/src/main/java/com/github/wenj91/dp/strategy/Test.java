package com.github.wenj91.dp.strategy;

public class Test {
    public static void main(String...args){
        StrategyContext strategyContext = new StrategyContext(new Strategy1());
        strategyContext.execute();

        strategyContext = new StrategyContext(new Strategy2());
        strategyContext.execute();
    }
}
