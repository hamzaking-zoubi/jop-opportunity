package com.example.myapplication.Recylers;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Handler;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.Calendar;

public class CalculateTime implements Runnable{
    private final static long MILLISECOND_IN_MINUTE = 60000,
                             MILLISECOND_IN_HOUR = MILLISECOND_IN_MINUTE * 60,
                             MILLISECOND_IN_DAY = MILLISECOND_IN_HOUR * 24,
                             MILLISECOND_IN_WEEK = MILLISECOND_IN_DAY * 7;
    TimeApiRunnable fd;
    private TextView mTextView;
    private Resources mResources;
    private long mMillisecond,
                 currDate;

    private Calendar date;

    private Handler handler;
    private UpdateRunnable runnable;
    private Thread thread;

    public CalculateTime(TextView textView, long millisecond, Resources resources) {
        this.mTextView = textView;
        this.mMillisecond = millisecond;
        this.mResources = resources;

        date = Calendar.getInstance();
        date.setTimeInMillis(millisecond);

        handler = new Handler();
        runnable = new UpdateRunnable();
        updateTime();
    }

    public void setTextView(TextView textView) {
        this.mTextView = textView;
    }

    public void setMillisecond(long millisecond) {
        this.mMillisecond = millisecond;
        date.setTimeInMillis(millisecond);

        shutDawn();
        updateTime();
    }

    public void shutDawn() {
        handler.removeCallbacksAndMessages(null);
        thread.interrupt();
    }

    private void updateTime() {

        thread = new Thread(new TimeApiRunnable() {

            @Override
            public void onCompletionListener(long millisecond) {
                //Be careful this function will execute in background thread
                currDate = millisecond;

                //This code will be executing in UI thread
                //handler object created in UI thread
                handler.post(runnable);
            }

            @Override
            public void onFailureListener(String errorMessage) {
                //Be careful this function will execute in background thread

                //This code will be executing in UI thread
                //handler object created in UI thread
                handler.post(new Runnable() {

                    @SuppressLint("SetTextI18n")//to skip concatenation warning
                    @Override
                    public void run() {
                        mTextView.setText(
                                mResources.getString(R.string.since) + " " +
                                        date.get(Calendar.YEAR) + "/" +
                                        date.get(Calendar.MONTH) + "/" +
                                        date.get(Calendar.DAY_OF_MONTH)
                        );
                    }

                });

            }

        });

        thread.start();
    }

    class UpdateRunnable implements Runnable {
        @SuppressLint("SetTextI18n")//to skip concatenation warning
        @Override
        public void run() {
            long timeAgo = currDate - mMillisecond;

            if (timeAgo < MILLISECOND_IN_MINUTE) {
                mTextView.setText(mResources.getString(R.string.now));

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_MINUTE - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_MINUTE * 2) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.one_minute)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_MINUTE * 2 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_MINUTE * 3) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.tow_minutes)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_MINUTE * 3 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_MINUTE * 11) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        timeAgo / MILLISECOND_IN_MINUTE + " " +
                        mResources.getString(R.string.for_ten_minutes)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_MINUTE - timeAgo % MILLISECOND_IN_MINUTE);
            }
            else if (timeAgo < MILLISECOND_IN_HOUR) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        timeAgo / MILLISECOND_IN_MINUTE + " " +
                        mResources.getString(R.string.minutes)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_MINUTE - timeAgo % MILLISECOND_IN_MINUTE);
            }
            else if (timeAgo < MILLISECOND_IN_HOUR * 2) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.one_hour)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_HOUR * 2 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_HOUR * 3) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.tow_hours)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_HOUR * 3 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_HOUR * 11) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        timeAgo / MILLISECOND_IN_HOUR + " " +
                        mResources.getString(R.string.for_ten_hours)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_HOUR - timeAgo % MILLISECOND_IN_HOUR);
            }
            else if (timeAgo < MILLISECOND_IN_DAY) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        timeAgo / MILLISECOND_IN_HOUR + " " +
                        mResources.getString(R.string.hours)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_HOUR - timeAgo % MILLISECOND_IN_HOUR);
            }
            else if (timeAgo < MILLISECOND_IN_DAY * 2) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.one_day)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_DAY * 2 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_DAY * 3) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.tow_days)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_DAY * 3 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_WEEK) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        timeAgo / MILLISECOND_IN_DAY + " " +
                        mResources.getString(R.string.for_ten_days)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_DAY - timeAgo % MILLISECOND_IN_DAY);
            }
            else if (timeAgo < MILLISECOND_IN_WEEK * 2) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.one_week)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_WEEK * 2 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_WEEK * 3) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " " +
                        mResources.getString(R.string.tow_weeks)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_WEEK * 3 - timeAgo);
            }
            else if (timeAgo < MILLISECOND_IN_WEEK * 4) {
                mTextView.setText(
                        mResources.getString(R.string.from) + " 3 " +
                        mResources.getString(R.string.for_ten_weeks)
                );

                runnable = new UpdateRunnable();
                handler.postDelayed(this, MILLISECOND_IN_WEEK * 4 - timeAgo);
            }
            else {
                mTextView.setText(
                        mResources.getString(R.string.since) + " " +
                        date.get(Calendar.YEAR) + "/" +
                        date.get(Calendar.MONTH) + "/" +
                        date.get(Calendar.DAY_OF_MONTH)
                );
            }

        }

    }

    @Override
    public void run() {
        updateTime();
    }

}
