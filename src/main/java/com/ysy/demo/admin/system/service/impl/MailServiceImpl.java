package com.ysy.demo.admin.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ysy.demo.admin.system.service.MailService;
import com.ysy.demo.admin.core.model.HandelResult;
import com.ysy.demo.admin.system.entity.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private Map<String, MailTemplate> codeTemplateMap;

    @Value("${app.mail.template-map:}")
    private void setCodeTemplateMap(String jsonStr) {
        this.codeTemplateMap = getJsonMap(jsonStr);
    }

    @Value("${app.mail.from:}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public HandelResult send(String code, String to, Object... params) {
        log.info("send code={}, to={}", code, to);
        MailTemplate template = codeTemplateMap.get(code);
        if (template == null) {
            log.info("send template no config code={}", code);
            return HandelResult.builder().message("template no config").build();
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(template.getTitle());
            message.setText(String.format(template.getContent(), params));
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("send exception", e);
            return HandelResult.builder().message("send failed").build();
        }
        log.info("send success");
        return HandelResult.SUCCESS;
    }

    private Map<String, MailTemplate> getJsonMap(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return Collections.emptyMap();
        }
        return JSON.parseObject(jsonStr, new TypeReference<Map<String, MailTemplate>>() {
        });
    }
}
