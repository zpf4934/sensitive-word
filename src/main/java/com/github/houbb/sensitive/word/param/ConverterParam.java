package com.github.houbb.sensitive.word.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConverterParam {
    private String query;
    private String area = "zh"; // 地区，zh:大陆，tw:台湾
}
