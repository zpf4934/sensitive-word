package com.github.houbb.sensitive.word.support.database;

import com.github.houbb.heaven.util.io.StreamUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SqliteDB {
    Logger logger = LogManager.getLogger(SqliteDB.class);
    public Connection connection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String name = Objects.requireNonNull(getClass().getClassLoader().getResource("")).getPath() + "sensitive.db";
        return DriverManager.getConnection("jdbc:sqlite:" + name);
    }

    public void create_table() throws SQLException, ClassNotFoundException {
        logger.info("创建表结构");
        Connection conn = connection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS sensitive_words (\n" +
                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  word TEXT\n" +
                ");");
        statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS \"unq_word\"\n" +
                "ON \"sensitive_words\" (\n" +
                "  \"word\"\n" +
                ");");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS sensitive_words_deny (\n" +
                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  word TEXT\n" +
                ");");
        statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS \"unq_word_deny\"\n" +
                "ON \"sensitive_words_deny\" (\n" +
                "  \"word\"\n" +
                ");");
        statement.close();
    }

    public List<String> show_tables() throws SQLException, ClassNotFoundException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        Connection conn = connection();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<String> results = new ArrayList<String>();
        while (rs.next()) {
            String word = rs.getString("name");
            results.add(word);
        }
        rs.close();
        statement.close();
        return results;
    }

    public void init_table() throws SQLException, ClassNotFoundException {
        logger.info("初始化数据库");
        List<String> tables = show_tables();
        create_table();
        if (!tables.contains("sensitive_words")){
            List<String> results = StreamUtil.readAllLines("/dict.txt");
            insert("sensitive_words", results);
        }
        if (!tables.contains("sensitive_words_deny")){
            List<String> results = StreamUtil.readAllLines("/sensitive_word_deny.txt");
            insert("sensitive_words_deny", results);
        }
    }

    public List<String> select(String table) throws SQLException, ClassNotFoundException {
        String sql = "select word from " + table;
        logger.info(sql);
        Connection conn = connection();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<String> results = new ArrayList<String>();
        while (rs.next()) {
            String word = rs.getString("word");
            results.add(word);
        }
        rs.close();
        statement.close();
        return results;
    }

    public void insert(String table, String word) throws SQLException, ClassNotFoundException {
        String sql = "insert or ignore into " + table + "(word) values('"+ word + "')";
        Connection conn = connection();
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
        stat.close();
    }

    public void insert(String table, List<String> words) throws SQLException, ClassNotFoundException {
        int n = 0;
        Connection conn = connection();
        conn.setAutoCommit(false);
        Statement stat = conn.createStatement();
        for (String word:words){
            String sql = "insert or ignore into " + table + "(word) values('"+ word + "')";
            stat.addBatch(sql);
            System.out.printf("%s表插入数据进度：%.2f \r", table, ++n / (float) words.size());
            if (n % 100 == 0){
                stat.executeBatch();
            }
        }
        stat.executeBatch();
        conn.commit();
        stat.close();
    }

    public void delete(String table, String word) throws SQLException, ClassNotFoundException {
        String sql = "delete from " + table + " where word = '"+ word + "'";
        Connection conn = connection();
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
        stat.close();
    }

    public void delete(String table, List<String> words) throws SQLException, ClassNotFoundException {
        int n = 0;
        Connection conn = connection();
        conn.setAutoCommit(false);
        Statement stat = conn.createStatement();
        for (String word:words){
            String sql = "delete from " + table + " where word = '"+ word + "'";
            stat.addBatch(sql);
            System.out.printf("%s表删除数据进度：%.2f \r", table, ++n / (float) words.size());
            if (n % 100 == 0){
                stat.executeBatch();
            }
        }
        stat.executeBatch();
        conn.commit();
        stat.close();
    }
}
