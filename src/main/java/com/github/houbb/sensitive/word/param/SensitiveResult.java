package com.github.houbb.sensitive.word.param;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveResult {
    private int code=200;
    private String meg = "执行成功";
    private Object result;
}
