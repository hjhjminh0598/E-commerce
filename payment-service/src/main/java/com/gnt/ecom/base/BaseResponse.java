package com.gnt.ecom.base;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    private static final String DATA_RETRIEVED_SUCCESS = "Data retrieved successfully";

    private static final String DATA_NOT_FOUND = "Data not found";

    private static final String SUCCESS = "Success";

    private boolean success;
    private String message;
    private int code;
    private T data;

    public static BaseResponse<Void> successNoData() {
        return success(null, SUCCESS);
    }

    public static <T> BaseResponse<T> success(T data) {
        return success(data, DATA_RETRIEVED_SUCCESS);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return BaseResponse.<T>builder()
                .success(true)
                .message(message)
                .code(HttpStatus.OK.value())
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> failure() {
        return failure(DATA_NOT_FOUND);
    }

    public static <T> BaseResponse<T> failure(Throwable e) {
        return failure(e.getMessage());
    }

    public static <T> BaseResponse<T> failure(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .code(HttpStatus.BAD_REQUEST.value())
                .data(null)
                .build();
    }
}
