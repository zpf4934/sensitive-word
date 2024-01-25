package com.github.houbb.sensitive.word.bs;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * <p> project: sensitive-word-SensitiveWordBsTest </p>
 * <p> create on 2020/1/7 23:43 </p>
 *
 * @author Administrator
 * @since 0.0.9
 */
@Ignore
public class SensitiveWordBsEmailTest {

    /**
     * 邮箱测试
     * @since 0.0.9
     */
    @Test
    public void emailEnglishTest() {
        final String text = "楼主好人，邮箱 sensitiveword@xx.com";

        List<String> wordList = SensitiveWordBs.newInstance().init().findAll(text);
        Assert.assertEquals("[sensitiveword@xx.com]", wordList.toString());
        System.out.println(wordList);
    }

    /**
     * 邮箱测试
     * @since 0.0.9
     */
    @Test
    public void emailNumberTest() {
        final String text = "楼主好人，邮箱 123456789@xx.com";

        List<String> wordList = SensitiveWordBs.newInstance().init().findAll(text);
        Assert.assertEquals("[123456789, xx.com]", wordList.toString());
    }

    @Test
    public void emailTest() {
        final String text = "你我.他你";
        List<String> wordList = SensitiveWordBs.newInstance().init().findAll(text);
        Assert.assertEquals("[]", wordList.toString());
    }

}
