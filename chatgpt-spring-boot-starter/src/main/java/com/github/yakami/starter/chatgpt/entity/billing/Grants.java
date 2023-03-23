package com.github.yakami.starter.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author plexpt
 */
@Data
public class Grants {
    private String object;
    @JsonProperty("data")
    private List<Datum> data;
}
