package com.github.houbb.sensitive.word.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveParam {
    private String query;
    private boolean ignoreCase = true; // 是否忽略大小写
    private boolean ignoreWidth = true; // 是否忽略全角、半角
    private boolean ignoreNumStyle = true;  // 是否忽略数字样式
    private boolean ignoreChineseStyle = true;  // 是否忽略中文样式
    private boolean ignoreEnglishStyle = true;  // 是否忽略英文样式
    private boolean ignoreRepeat = false;   // 是否忽略重复
    private boolean enableNumCheck = false;  // 启用数字检测
    private boolean enableEmailCheck = false;    // 启用邮箱检测
    private boolean enableUrlCheck = false;  // 启用 URL 检测
    private boolean enableWordCheck = true; // 单词校验
    private boolean ignoreSpecial = true; // 是否忽略特殊字符
    private int numCheckLen = 8;    // 检测数字时的长度
    private char wordReplace = '*';    // 替换字符
    private List<String> wordDeny; // 禁止的单词
}
