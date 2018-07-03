package com.github.wenj91.dp.strategy;

public class StrategyContext {
    private Strategy strategy;

    public StrategyContext(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute(){
        strategy.strategy();
    }
}
