package com.github.houbb.sensitive.word.controller;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.param.ConverterParam;
import com.github.houbb.sensitive.word.param.SensitiveParam;
import com.github.houbb.sensitive.word.param.SensitiveResult;
import com.github.houbb.sensitive.word.support.database.SqliteDB;
import com.github.houbb.sensitive.word.support.ignore.SensitiveWordCharIgnores;
import com.github.houbb.sensitive.word.support.replace.WordReplaces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Controller
@ResponseBody
@RequestMapping("/sensitive")
public class SensitiveController {
    Logger logger = LogManager.getLogger(SensitiveController.class);

    private final SensitiveWordBs sensitive = SensitiveWordBs.newInstance();

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {
        logger.info("初始化检测器");
        SqliteDB sqlite = new SqliteDB();
        sqlite.init_table();
        SensitiveParam param = new SensitiveParam();
        sensitive.ignoreCase(param.isIgnoreCase())
                .ignoreWidth(param.isIgnoreWidth())
                .ignoreNumStyle(param.isIgnoreNumStyle())
                .ignoreChineseStyle(param.isIgnoreChineseStyle())
                .ignoreEnglishStyle(param.isIgnoreEnglishStyle())
                .ignoreRepeat(param.isIgnoreRepeat())
                .enableNumCheck(param.isEnableNumCheck())
                .enableEmailCheck(param.isEnableEmailCheck())
                .enableUrlCheck(param.isEnableUrlCheck())
                .enableWordCheck(param.isEnableWordCheck())
                .numCheckLen(param.getNumCheckLen())
                .wordReplace(WordReplaces.chars(param.getWordReplace()))
                .charIgnore(SensitiveWordCharIgnores.specialChars())
                .init();
    }

    public void refresh(SensitiveParam param){
        boolean flag = false;
        if(!param.isIgnoreCase()){
            sensitive.ignoreCase(false);
            flag = true;
        }
        if(!param.isIgnoreWidth()){
            sensitive.ignoreWidth(false);
            flag = true;
        }
        if(!param.isIgnoreNumStyle()){
            sensitive.ignoreNumStyle(false);
            flag = true;
        }
        if(!param.isIgnoreChineseStyle()){
            sensitive.ignoreChineseStyle(false);
            flag = true;
        }
        if(!param.isIgnoreEnglishStyle()){
            sensitive.ignoreEnglishStyle(false);
            flag = true;
        }
        if(param.isIgnoreRepeat()){
            sensitive.ignoreRepeat(true);
            flag = true;
        }
        if(param.isEnableNumCheck()){
            sensitive.enableNumCheck(true);
            flag = true;
        }
        if(param.isEnableEmailCheck()){
            sensitive.enableEmailCheck(true);
            flag = true;
        }
        if(param.isEnableUrlCheck()){
            sensitive.enableUrlCheck(true);
            flag = true;
        }
        if(!param.isEnableWordCheck()){
            sensitive.enableWordCheck(false);
            flag = true;
        }
        if(!param.isIgnoreSpecial()){
            sensitive.charIgnore(SensitiveWordCharIgnores.defaults());
            flag = true;
        }
        if(param.getNumCheckLen() != 8){
            sensitive.numCheckLen(param.getNumCheckLen());
            flag = true;
        }
        if(param.getWordReplace() != '*'){
            sensitive.wordReplace(WordReplaces.chars(param.getWordReplace()));
            flag = true;
        }
        if (flag){
            logger.info("更新检测器");
            sensitive.init();
        }
    }

    @RequestMapping(value = "/find_all", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult findAll(@RequestBody SensitiveParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        refresh(param);
        List<String> result = new ArrayList<>(new HashSet<>(sensitive.findAll(param.getQuery())));
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/contains", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult contains(@RequestBody SensitiveParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        refresh(param);
        boolean result = sensitive.contains(param.getQuery());
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/find_first", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult findFirst(@RequestBody SensitiveParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        refresh(param);
        String result = sensitive.findFirst(param.getQuery());
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult replace(@RequestBody SensitiveParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        refresh(param);
        String result = sensitive.replace(param.getQuery());
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/add_deny", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult add_deny(@RequestBody SensitiveParam param) {
        logger.info("添加停用词");
        logger.info(param.getWordDeny());
        SensitiveResult response = new SensitiveResult();
        SqliteDB sqlite = new SqliteDB();
        try {
            sqlite.insert("sensitive_words_deny", param.getWordDeny());
            logger.info("添加停用词成功");
            sensitive.init();
            response.setResult("添加成功");
            export_data();
        }
        catch (ClassNotFoundException | SQLException e){
            logger.error("添加停用词失败");
            logger.error(e);
            response.setResult("添加失败");
            response.setCode(301);
        }
        return response;
    }

    @RequestMapping(value = "/del_deny", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult del_deny(@RequestBody SensitiveParam param) {
        logger.info("移除停用词");
        logger.info(param.getWordDeny());
        SensitiveResult response = new SensitiveResult();
        SqliteDB sqlite = new SqliteDB();
        try {
            sqlite.delete("sensitive_words_deny", param.getWordDeny());
            sqlite.delete("sensitive_words", param.getWordDeny());
            logger.info("移除停用词成功");
            sensitive.init();
            response.setResult("移除成功");
            export_data();
        }
        catch (ClassNotFoundException | SQLException e){
            logger.error("移除停用词失败");
            logger.error(e);
            response.setResult("移除失败");
            response.setCode(302);
        }
        return response;
    }


    public void export_data(){
        List<String> tables = Arrays.asList("sensitive_words","sensitive_words_deny");
        List<String> files = Arrays.asList("dict.txt","sensitive_word_deny.txt");
        for (int i=0; i < tables.size(); i++){
            String file = Objects.requireNonNull(getClass().getClassLoader().getResource(files.get(i))).getPath();
            try{
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                SqliteDB sqlite = new SqliteDB();
                List<String> results = sqlite.select(tables.get(i));
                if (!results.isEmpty()){
                    for (String word:results.subList(0,results.size()-1)){
                        out.write(word);
                        out.newLine();
                    }
                    out.write(results.get(results.size() - 1));
                }
                out.close();
                logger.info(tables.get(i) + ":导出成功");
            } catch (IOException | SQLException | ClassNotFoundException e){
                logger.error(e);
            }
        }
    }

}
