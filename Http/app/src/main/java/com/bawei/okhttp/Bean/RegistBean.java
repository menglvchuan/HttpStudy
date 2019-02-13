package com.bawei.okhttp.Bean;

/**
 * Time:2019/2/13
 * <p>
 * Author:肖佳莹
 * <p>
 * Description:
 */
public class RegistBean {
    private String message;
    private String status;

    public RegistBean(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RegistBean{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
