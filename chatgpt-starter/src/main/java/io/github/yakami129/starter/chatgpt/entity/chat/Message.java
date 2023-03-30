package io.github.yakami129.starter.chatgpt.entity.chat;

import lombok.*;

/**
 * @author plexpt
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    /**
     * 目前支持三中角色参考官网，进行情景输入：https://platform.openai.com/docs/guides/chat/introduction
     */
    private String role;
    private String content;

    public static Message of(String content) {

        return new Message(Role.USER.getValue(), content);
    }

    public static Message ofSystem(String content) {

        return new Message(Role.SYSTEM.getValue(), content);
    }

    @Getter
    @AllArgsConstructor
    public enum Role {

        SYSTEM("system"),
        USER("user"),

        ASSISTANT("assistant"),
        ;
        private String value;
    }

}
