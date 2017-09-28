package com.rant.action.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.rant.config.Utils;

/**
 * Forgot action class
 * 
 * @author Austin Delamar
 * @date 12/19/2016
 */
public class ForgotAction extends ActionSupport
        implements
            ServletResponseAware,
            ServletRequestAware {

    private static final long serialVersionUID = 1L;
    private Map<String, Object> sessionAttributes = null;
    private String email; // need email to send reminders or resets
    private String code;
    private String type; // 'username' or 'password' or 'twofactor'

    private boolean remind; // true if they forgot username
    private boolean reset; // true if they forgot password
    private boolean recover; // true if they forgot two factor

    private static final int LOCKOUT_MINS = 30; // minutes
    private static final int MAX_ATTEMPTS = 3;
    private int attempts = 0;
    private long lastAttempt = 0;

    // JSON response
    private String forgot;
    private String error;
    private String message;
    private Map<String, String> data;

    @Override
    public String execute() {

        try {
            sessionAttributes = ActionContext.getContext().getSession();
            // are they already logged in?
            if (sessionAttributes.get("login") != null) {
                throw new Exception("You are already logged in. Did you forget?");
            }

            // check if locked out
            // check inputs
            if (!isLockedOut() && validParameters()) {

                // if email is good, and remind, then send reminder email.
                // TODO

                // if email is good, and reset, then send email link to reset
                // password.
                // Generate a unique token that can only be used once.
                // TODO

                if (remind) {
                    message = "You have been sent a reminder. Please check your inbox for your Username.";
                } else if (reset) {
                    message = "You have been sent a reset token. Please check your inbox to reset your password.";
                } else if (recover) {
                    message = "You have been sent a recovery token. Please check your inbox to login.";
                }
                forgot = "ok";
                data = new HashMap<String, String>();
                data.put("email", email);
                data.put("time", Utils.getDateIso8601());

                sessionAttributes.remove("attempts");
                sessionAttributes.remove("lastAttempt");
            }

        } catch (Exception e) {
            error = "error";
            message = e.getMessage();
        }

        // return response
        System.out.println(message);
        return NONE;
    }

    /**
     * Check email parameter.
     * 
     * @return true if ok
     * @throws Exception
     *             if invalid
     */
    private boolean validParameters() throws Exception {
        if (email == null || email.isEmpty()) {
            throw new Exception("Invalid or missing email.");
        } else if (!Utils.isValidEmail(email)) {
            throw new Exception("Invalid email address. Double-check and try again.");
        } else if (!remind && !reset && !recover) {
            throw new Exception(
                    "Missing expected boolean option. Please add remind, reset, or recover.");
        } else {
            return true;
        }
    }

    /**
     * Check if the user has attempted too many times, and lock them out if they have.
     * 
     * @return true if locked out
     * @throws Exception
     *             if locked out
     */
    private boolean isLockedOut() throws Exception {
        // count login attempts
        // and remember when their last attempt was
        if (sessionAttributes.get("attempts") == null) {
            attempts = 1;
        } else {
            attempts = (int) sessionAttributes.get("attempts");
            attempts++;
        }
        if (sessionAttributes.get("lastAttempt") == null) {
            lastAttempt = System.currentTimeMillis();
        } else {
            lastAttempt = (long) sessionAttributes.get("lastAttempt");
        }
        sessionAttributes.put("attempts", attempts);
        sessionAttributes.put("lastAttempt", lastAttempt);

        // lockout for 30 min, if they failed 3 times
        if (attempts >= MAX_ATTEMPTS) {

            if (System.currentTimeMillis() >= (lastAttempt + (LOCKOUT_MINS * 60 * 1000))) {
                // its been 30mins or more, so unlock
                sessionAttributes.remove("attempts");
                sessionAttributes.remove("lastAttempt");
                System.err.println("Unknown user has waited " + LOCKOUT_MINS + " min, proceed. ("
                        + servletRequest.getRemoteAddr() + ")(" + servletRequest.getRemoteHost()
                        + ")");
                return false;
            } else {
                // they have already been locked out
                System.err.println("Unknown user has been locked out for " + LOCKOUT_MINS
                        + " min. (" + servletRequest.getRemoteAddr() + ")("
                        + servletRequest.getRemoteHost() + ") ");
                throw new Exception("You have been locked out for the next " + LOCKOUT_MINS
                        + " minutes, for too many attempts.");
            }
        }
        return false;
    }

    public void setSession(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    protected HttpServletResponse servletResponse;

    @Override
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    protected HttpServletRequest servletRequest;

    @Override
    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code != null && code.length() > 6) {
            this.code =  Utils.removeBadChars(code.substring(0, 6));
        } else {
            this.code = Utils.removeBadChars(code);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null)
            type = "username";
        this.type = Utils.removeBadChars(type);
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isRecover() {
        return recover;
    }

    public void setRecover(boolean recover) {
        this.recover = recover;
    }

    public String getForgot() {
        return forgot;
    }

    public void setForgot(String forgot) {
        this.forgot = forgot;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}