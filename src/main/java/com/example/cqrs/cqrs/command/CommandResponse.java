package com.example.cqrs.cqrs.command;

import java.util.function.Consumer;

public record CommandResponse<R>(R response, boolean success) {

    public static <R> CommandResponse<R> ok(R response) {
        return new CommandResponse<>(response, true);
    }

    public static <R> CommandResponse<R> error(R response) {
        return new CommandResponse<>(response, false);
    }

    public void ifSuccess(Consumer<R> consumer) {
        if (success) {
            consumer.accept(response);
        }
    }

    public void ifFailure(Consumer<R> consumer) {
        if (!success) {
            consumer.accept(response);
        }
    }

    public R getResponse() {
        return response;
    }

    public boolean isSuccess() {
        return success;
    }
}
