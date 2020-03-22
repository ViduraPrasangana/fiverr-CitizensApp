package com.adamhussain.citizensapp.home.payment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

public class PayItem implements Parcelable {
    private String title;

    @DrawableRes
    private int bgRes;


    public PayItem(String title, @DrawableRes int bgRes) {
        this.title = title;
        this.bgRes = bgRes;
    }

    protected PayItem(Parcel in) {
        title = in.readString();
        bgRes = in.readInt();
    }

    public static final Creator<PayItem> CREATOR = new Creator<PayItem>() {
        @Override
        public PayItem createFromParcel(Parcel in) {
            return new PayItem(in);
        }

        @Override
        public PayItem[] newArray(int size) {
            return new PayItem[size];
        }
    };

    public String getTitle() {
        return title;
    }


    public int getBgRes() {
        return bgRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeInt(bgRes);
    }
}
