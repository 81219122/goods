package com.zhangrun.entity;

import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 14:01
 * 分页bean  在各层之间传递
 */
public class PageBean<T> {
    private int pc; //当前页码
    private int tr;  //总记录数
    private int ps ;  //每页记录数
    private String url ;//请求路径和参数 例如 /bookservlet？method=findxxx&cid=1&bname=2
    private List<T> beanList;  //当前页内容
    public int getTp(){//获取总页数
        return tr % ps == 0 ? tr/ps :(tr/ps) +1;
    }

    @Override
    public String toString() {
        return "PageBean{" + "pc=" + pc + ", tr=" + tr + ", ps=" + ps + ", url='" + url + '\'' + ", beanList=" + beanList + '}';
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }
}
