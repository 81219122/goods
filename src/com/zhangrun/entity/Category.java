package com.zhangrun.entity;

import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/19 17:12
 * 分类模块 分书类 然后他的父分类 c语言程序设计...  子分类
 * 双向自身关联  子分类关联父分类 父分类关联子分类 用了一张表
 */
public class Category {
    private String cid;//主键
    private String cname;//分类名称
    private Category parent;  //父分类 pid
    private String desc;   //分类描述
    private List<Category> children; //子分类

    @Override
    public String toString() {
        return "Category{" + "cid='" + cid + '\'' + ", cname='" + cname + '\'' + ", parent=" + parent + ", desc='" + desc + '\'' + ", children=" + children + '}';
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
