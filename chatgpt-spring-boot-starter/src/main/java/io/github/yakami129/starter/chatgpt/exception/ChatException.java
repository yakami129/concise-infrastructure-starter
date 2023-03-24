package io.github.yakami129.starter.chatgpt.exception;

/**
 * Created by alan on 2023/3/23.
 */
public class ChatException extends RuntimeException {

    public ChatException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
    }
}
