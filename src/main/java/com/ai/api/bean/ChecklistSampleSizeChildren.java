package com.ai.api.bean;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean
 * Creation Date   : 2016/9/20 16:28
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class ChecklistSampleSizeChildren {
    private String value;
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ChecklistSampleSizeChildren{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
