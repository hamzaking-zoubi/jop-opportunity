package com.example.myapplication.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAOcgiExs:APA91bGzDmY0khTnUWuI-qTvR3OGRfWR9S24jW6P5r84SA-ePKWDspT00OJvWUs5FFmuUXeuCn2G8kcbIDggIMmvu8WDLWrQuXKShbWVlHPKYQctwFs8faJAjC927uBPozflV0HcvEaz"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

