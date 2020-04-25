package com.zhangrun.entity;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 13:34
 */
public class ResultInfo {
    private String errorMsg;
    private boolean flag;
    private Object data;

    @Override
    public String toString() {
        return "ResultInfo{" + "errorMsg='" + errorMsg + '\'' + ", flag=" + flag + ", data=" + data + '}';
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
