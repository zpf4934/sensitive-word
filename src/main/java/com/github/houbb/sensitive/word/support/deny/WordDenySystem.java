package com.github.houbb.sensitive.word.support.deny;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.util.io.StreamUtil;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.support.database.SqliteDB;
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
public class WordDenySystem implements IWordDeny {
    Logger logger = LogManager.getLogger(WordDenySystem.class);
    /**
     * @since 0.3.0
     */
    private static final IWordDeny INSTANCE = new WordDenySystem();

    public static IWordDeny getInstance() {
        return INSTANCE;
    }

    @Override
    public List<String> deny() {
        try {
            SqliteDB sqlite = new SqliteDB();
            List<String> results = sqlite.select("sensitive_words");
            results.addAll(sqlite.select("sensitive_words_deny"));
            return results;
        }
        catch (ClassNotFoundException | SQLException e){
            logger.error(e);
            return null;
        }
//        List<String> results = StreamUtil.readAllLines("/dict.txt");
//        results.addAll(StreamUtil.readAllLines("/sensitive_word_deny.txt"));
//        return results;
    }

}
