package com.fireflylearning.tasksummary.activities.login.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fireflylearning.tasksummary.FireflyRequestQueue;
import com.fireflylearning.tasksummary.activities.login.view.LoginActivity;
import com.fireflylearning.tasksummary.logic.database.TasksDB;

/**
 * Created by javie on 14/01/2018.
 */

public class LoginPresenter {
    private Context context;
    LoginActivity view;

    public LoginPresenter(Context context, LoginActivity view) {
        this.context = context;
        this.view = view;
    }

    public void checkToken(@NonNull String host, @NonNull String token) {
        FireflyRequestQueue.initialise(context, host, token);

        FireflyRequestQueue.getInstance().RunGetRequest(
                "login/api/checktoken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(!response.equals("OK")) {
                            view.loginCorrect();

                        } else {
                            view.loginError();
                            createDB();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        view.showTokenError(error.networkResponse.statusCode == 401 ? LoginActivity.tokenError.invalidToken : LoginActivity.tokenError.hostError);
                    }
                }
        );
    }

    private boolean createDB() {
        try {
            TasksDB db = new TasksDB(context);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
