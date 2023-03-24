package io.github.yakami129.starter.chatgpt.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@UtilityClass
public class SseHelper {

    public void complete(SseEmitter sseEmitter) {
        try {
            sseEmitter.complete();
        } catch (Exception e) {
            log.warn("[WARN] {}", e.getMessage());
        }
    }

    public void send(SseEmitter sseEmitter, Object data) {
        try {
            sseEmitter.send(data);
        } catch (Exception e) {
            log.warn("[WARN] {}", e.getMessage());
        }
    }
}
