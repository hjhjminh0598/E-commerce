package com.ecom.user.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    private boolean success;
    private String message;
    private T data;

    private static final String DATA_RETRIEVED_SUCCESS = "Data retrieved successfully";

    private static final String DATA_NOT_FOUND = "Data not found";

    private static final String SUCCESS = "Success";

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
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> failure() {
        return failure(DATA_NOT_FOUND);
    }

    public static <T> BaseResponse<T> failure(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
