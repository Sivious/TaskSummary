package com.fireflylearning.tasksummary.activities.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.fireflylearning.tasksummary.R;
import com.fireflylearning.tasksummary.activities.tasksList.view.TaskListActivity;
import com.fireflylearning.tasksummary.activities.login.presenter.LoginPresenter;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //region UI references.

    EditText mHostView;
    private EditText mTokenView;
    private View mProgressView;
    private View mLoginFormView;

    public enum tokenError {
        networkError, hostError, invalidToken
    }
    //endregion

    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPresenter(getApplicationContext(), this);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mHostView = (EditText) findViewById(R.id.host);

        mTokenView = (EditText) findViewById(R.id.token);
        mTokenView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.token_action || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mTokenView.requestFocus();

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {

        // Reset errors.
        mHostView.setError(null);
        mTokenView.setError(null);

        // Store values at the time of the login attempt.
        String host = mHostView.getText().toString();
        String token = mTokenView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(token)) {
            mTokenView.setError(getString(R.string.error_field_required));
            focusView = mTokenView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(host)) {
            mHostView.setError(getString(R.string.error_field_required));
            focusView = mHostView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            presenter.checkToken(host, token);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void showTokenError(tokenError tokenError) {
        showProgress(false);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            dialog = new AlertDialog.Builder(this);
        }
        dialog.setCancelable(true);
        dialog.setTitle(R.string.title_error);

        String message;
        switch (tokenError) {
            case networkError:
                message = getString(R.string.error_internet_connection);
                break;
            case hostError:
                message = getString(R.string.error_invalid_host);
                break;
            case invalidToken:
                message = getString(R.string.error_invalid_token);
                break;
            default:
                message = "";
        }
        dialog.setMessage(message);

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void performLogin() {
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
    }

    public void loginCorrect() {
        showProgress(false);
        showTokenError(LoginActivity.tokenError.hostError);
    }

    public void loginError() {
        showProgress(false);
        performLogin();
    }
}

