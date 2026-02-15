package com.finmanager.api;

public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private String error;

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Created successfully", data);
    }

    public static ApiResponse<?> error(int statusCode, String error) {
        return new ApiResponse<>(statusCode, error);
    }

    public static ApiResponse<?> notFound() {
        return new ApiResponse<>(404, "Resource not found");
    }

    public static ApiResponse<?> badRequest(String error) {
        return new ApiResponse<>(400, error);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
