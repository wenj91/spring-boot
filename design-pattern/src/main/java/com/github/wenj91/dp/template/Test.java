package com.github.wenj91.dp.template;

public class Test {
    public static void main(String...args){
        Template template = new TemplateImpl1();
        template.execute();

        template = new TemplateImpl2();
        template.execute();
    }
}
