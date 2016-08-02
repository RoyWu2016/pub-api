package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yan on 2016/8/2.
 */
public class ReportPreferenceBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String withAttachment;
    private String attType;
    private String reportContactName;
    private String reportTemplate;
    private int maxReportSize;
    private String disApproverName;
    private String sendMailToSupplier;
    private String sameDayReport;
    private String allowReportApprover;
    private List<ReportApproverBean> approvers;
    private List<ReportRejectCategoryBean> rejectCategories;
    private String rejectReasonSortBy;
    private String rejectReasonOther;

    public ReportPreferenceBean(){}

    public String getWithAttachment() {
        return withAttachment;
    }

    public void setWithAttachment(String withAttachment) {
        this.withAttachment = withAttachment;
    }

    public String getAttType() {
        return attType;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public String getReportContactName() {
        return reportContactName;
    }

    public void setReportContactName(String reportContactName) {
        this.reportContactName = reportContactName;
    }

    public String getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(String reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public int getMaxReportSize() {
        return maxReportSize;
    }

    public void setMaxReportSize(int maxReportSize) {
        this.maxReportSize = maxReportSize;
    }

    public String getDisApproverName() {
        return disApproverName;
    }

    public void setDisApproverName(String disApproverName) {
        this.disApproverName = disApproverName;
    }

    public String getSendMailToSupplier() {
        return sendMailToSupplier;
    }

    public void setSendMailToSupplier(String sendMailToSupplier) {
        this.sendMailToSupplier = sendMailToSupplier;
    }

    public String getSameDayReport() {
        return sameDayReport;
    }

    public void setSameDayReport(String sameDayReport) {
        this.sameDayReport = sameDayReport;
    }

    public String getAllowReportApprover() {
        return allowReportApprover;
    }

    public void setAllowReportApprover(String allowReportApprover) {
        this.allowReportApprover = allowReportApprover;
    }

    public List<ReportApproverBean> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<ReportApproverBean> approvers) {
        this.approvers = approvers;
    }

    public List<ReportRejectCategoryBean> getRejectCategories() {
        return rejectCategories;
    }

    public void setRejectCategories(List<ReportRejectCategoryBean> rejectCategories) {
        this.rejectCategories = rejectCategories;
    }

    public String getRejectReasonSortBy() {
        return rejectReasonSortBy;
    }

    public void setRejectReasonSortBy(String rejectReasonSortBy) {
        this.rejectReasonSortBy = rejectReasonSortBy;
    }

    public String getRejectReasonOther() {
        return rejectReasonOther;
    }

    public void setRejectReasonOther(String rejectReasonOther) {
        this.rejectReasonOther = rejectReasonOther;
    }
}
