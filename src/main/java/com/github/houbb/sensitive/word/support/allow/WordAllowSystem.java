package com.github.houbb.sensitive.word.support.allow;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.util.io.StreamUtil;
import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.support.database.SqliteDB;
import com.github.houbb.sensitive.word.support.deny.WordDenySystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * 系统默认的信息
 * @author binbin.hou
 * @since 0.0.13
 */
@ThreadSafe
public class WordAllowSystem implements IWordAllow {
    Logger logger = LogManager.getLogger(WordAllowSystem.class);
    /**
     * @since 0.3.0
     */
    private static final WordAllowSystem INSTANCE = new WordAllowSystem();

    public static WordAllowSystem getInstance() {
        return INSTANCE;
    }

    @Override
    public List<String> allow() {
        return StreamUtil.readAllLines("/sensitive_word_allow.txt");
    }

}
