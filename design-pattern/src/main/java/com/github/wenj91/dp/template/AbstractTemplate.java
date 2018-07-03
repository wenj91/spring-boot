package com.github.wenj91.dp.template;

public abstract class AbstractTemplate implements Template {
    @Override
    public void execute() {
        System.out.println("execute before");
        doExecute();
        System.out.println("execute after");
    }

    // template method
    protected abstract void doExecute();
}
