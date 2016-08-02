package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class PreferencesBean implements Serializable {
    private BookingPreferenceBean booking;

    private ReportPreferenceBean report;

    public BookingPreferenceBean getBooking() {
        return booking;
    }

    public void setBooking(BookingPreferenceBean booking) {
        this.booking = booking;
    }

    public ReportPreferenceBean getReport() {
        return report;
    }

    public void setReport(ReportPreferenceBean report) {
        this.report = report;
    }
}
