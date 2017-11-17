package com.pech.crashlib.reports;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yairp on 08/11/2017.
 */

public class Report implements Parcelable
{
    @JsonProperty("ReportID")
    private long mReportID;

    @JsonProperty("When")
    private String mWhen;

    @JsonProperty("Exception")
    private String mException;

    @JsonProperty("StackTrace")
    private String mStackTrace;

    @JsonProperty("Logcat")
    private String mLogcat;

    @JsonProperty("AndroidVersion")
    private String mAndroidVersion;

    @JsonProperty("Device")
    private String mDevice;

    public Report()
    {
    }

    private void setReportID(long reportID)
    {
        mReportID = reportID;
    }

    private void setWhen(String when)
    {
        mWhen = when;
    }

    private void setException(String exceptionData)
    {
        mException = exceptionData;
    }

    private void setStackTrace(String stackTrace)
    {
        mStackTrace = stackTrace;
    }

    private void setLogcat(String logcat)
    {
        mLogcat = logcat;
    }

    private void setAndroidVersion(String androidVersion)
    {
        mAndroidVersion = androidVersion;
    }

    private void setDevice(String device)
    {
        mDevice = device;
    }

    @JsonIgnore
    public long getReportID()
    {
        return mReportID;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(mReportID);
        dest.writeString(mWhen);
        dest.writeString(mException);
        dest.writeString(mStackTrace);
        dest.writeString(mLogcat);
        dest.writeString(mAndroidVersion);
        dest.writeString(mDevice);
    }

    public static final Creator<Report> CREATOR = new Creator<Report>()
    {
        @Override
        public Report createFromParcel(Parcel source)
        {
            Report report = new Report.Builder()
                    .reportID(source.readLong())
                    .when(source.readString())
                    .exception(source.readString())
                    .stackTrace(source.readString())
                    .logcat(source.readString())
                    .androidVersion(source.readString())
                    .device(source.readString())
                    .build();

            return report;
        }

        @Override
        public Report[] newArray(int size)
        {
            return new Report[0];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @JsonIgnoreProperties
    public static class Builder
    {
        private long mReportID;
        private String mWhen;
        private String mException;
        private String mStackTrace;
        private String mLogcat;
        private String mAndroidVersion;
        private String mDevice;

        public Builder()
        {
        }

        public Report build()
        {
            Report report = new Report();
            report.setReportID(mReportID);
            report.setWhen(mWhen);
            report.setException(mException);
            report.setStackTrace(mStackTrace);
            report.setLogcat(mLogcat);
            report.setAndroidVersion(mAndroidVersion);
            report.setDevice(mDevice);
            return report;
        }

        public Builder reportID(long reportID)
        {
            mReportID = reportID;
            return this;
        }

        public Builder when(String when)
        {
            mWhen = when;
            return this;
        }

        public Builder exception(String exceptionData)
        {
            mException = exceptionData;
            return this;
        }

        public Builder stackTrace(String stackTrace)
        {
            mStackTrace = stackTrace;
            return this;
        }

        public Builder logcat(String logcat)
        {
            mLogcat = logcat;
            return this;
        }

        public Builder androidVersion(String androidVersion)
        {
            mAndroidVersion = androidVersion;
            return this;
        }

        public Builder device(String device)
        {
            mDevice = device;
            return this;
        }
    }
}
