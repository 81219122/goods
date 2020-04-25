package com.zhangrun.entity;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 14:28
 * sql语句的条件
 */
public class Expression {
    private String name;  //名称
    private String operator;  //运算符
    private String value;       //值

    public Expression(String name, String operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Expression{" + "name='" + name + '\'' + ", operator='" + operator + '\'' + ", value='" + value + '\'' + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
