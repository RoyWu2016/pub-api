package com.ai.api.bean;

import java.util.List;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean
 * Creation Date   : 2016/9/20 15:06
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class ChecklistSampleSize {
    private String label;
    private List<ChecklistSampleSizeChildren> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ChecklistSampleSizeChildren> getChildren() {
        return children;
    }

    public void setChildren(List<ChecklistSampleSizeChildren> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ChecklistSampleSize{" +
                "label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
