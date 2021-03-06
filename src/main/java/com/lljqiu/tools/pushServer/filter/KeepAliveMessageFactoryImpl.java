/**
 * Project Name pushServer
 * File Name KeepAliveMessageFactoryImpl.java
 * Package Name com.lljqiu.tools.pushServer.filter
 * Create Time 2018年3月15日
 * Create by name：liujie -- email: jie_liu1@asdc.com.cn
 * Copyright © 2006, 2017, ASDC DAI. All rights reserved.
 */
package com.lljqiu.tools.pushServer.filter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.alibaba.fastjson.JSONObject;
import com.lljqiu.tools.pushServer.stack.BaseMessage;
import com.lljqiu.tools.pushServer.utils.Constants;

/** 
 * ClassName: KeepAliveMessageFactoryImpl.java <br>
 * Description: <br>
 * @author name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * @date: 2018年3月15日<br>
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    private static Log log = LogFactory.getLog(KeepAliveMessageFactoryImpl.class);

    public boolean isRequest(IoSession session, Object message) {
       
        BaseMessage command = null;
        try {
            command = (BaseMessage) message;
        } catch (Exception e) {
            return false;
        }
        if(command.getType() == Constants.T101){
            log.info("request keepalive ...");
            return true;
        }
        return false;
    }

    public boolean isResponse(IoSession session, Object message) {
        return false;
    }

    public Object getRequest(IoSession session) {
        return null;
    }

    public Object getResponse(IoSession session, Object request) {
        BaseMessage requestMessage = (BaseMessage) request;
        
        log.debug("服务端接收心跳请求数据：" + ToStringBuilder.reflectionToString(requestMessage));

        JSONObject json = new JSONObject();
        json.put("status", Constants.SUCCESS);
        json.put("result", "keep live success");
        //ResponseMessage responseMessage = new ResponseMessage(Constants.SUCCESS,Constants.T101);
        try {
            requestMessage.setBody(json.toString().getBytes(Constants.CHARTSET));
            requestMessage.setBodyLength(json.toString().getBytes(Constants.CHARTSET).length);
        } catch (UnsupportedEncodingException e) {
            log.debug("响应心跳请求异常" + e);
        }
        return requestMessage.toByte();
    }

}
