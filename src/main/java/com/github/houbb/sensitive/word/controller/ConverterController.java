package com.github.houbb.sensitive.word.controller;

import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.github.houbb.opencc4j.util.ZhTwConverterUtil;
import com.github.houbb.sensitive.word.param.ConverterParam;
import com.github.houbb.sensitive.word.param.SensitiveResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/converter")
public class ConverterController {
    Logger logger = LogManager.getLogger(ConverterController.class);

    @RequestMapping(value = "/contain_simple", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult containSimple(@RequestBody ConverterParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        boolean result;
        if ("tw".equals(param.getArea())){
             result = ZhTwConverterUtil.containsSimple(param.getQuery());
        }else {
             result = ZhConverterUtil.containsSimple(param.getQuery());
        }
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/contain_traditional", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult containsTraditional(@RequestBody ConverterParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        boolean result;
        if ("tw".equals(param.getArea())){
            result = ZhTwConverterUtil.containsTraditional(param.getQuery());
        }else {
            result = ZhConverterUtil.containsTraditional(param.getQuery());
        }
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/to_simple", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult toSimple(@RequestBody ConverterParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        String result;
        if ("tw".equals(param.getArea())){
            result = ZhTwConverterUtil.toSimple(param.getQuery());
        }else {
            result = ZhConverterUtil.toSimple(param.getQuery());
        }
        response.setResult(result);
        return response;
    }

    @RequestMapping(value = "/to_traditional", method = RequestMethod.POST)
    @ResponseBody
    public SensitiveResult toTraditional(@RequestBody ConverterParam param) {
        logger.info(param);
        SensitiveResult response = new SensitiveResult();
        String result;
        if ("tw".equals(param.getArea())){
            result = ZhTwConverterUtil.toTraditional(param.getQuery());
        }else {
            result = ZhConverterUtil.toTraditional(param.getQuery());
        }
        response.setResult(result);
        return response;
    }
}
