/**
 * Project Name pushServer
 * File Name MessageHandler.java
 * Package Name com.lljqiu.tools.pushServer.context
 * Create Time 2018年3月15日
 * Create by name：liujie -- email: jie_liu1@asdc.com.cn
 * Copyright © 2006, 2017, ASDC DAI. All rights reserved.
 */
package com.lljqiu.tools.pushServer.context;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.lljqiu.tools.pushServer.stack.BaseMessage;
import com.lljqiu.tools.pushServer.stack.SessionUsers;
import com.lljqiu.tools.pushServer.utils.Constants;

/** 
 * ClassName: MessageHandler.java <br>
 * Description: 服务端IO处理器<br>
 * @author name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * @date: 2018年3月15日<br>
 */
public class MessageHandler extends IoHandlerAdapter {
    private static Log            log          = LogFactory.getLog(MessageHandler.class);

    protected static final String TSSESSIONKEY = "MPSession_Key";


    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.debug("sessionCreated()...sessionId=" + session.getId());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.debug("sessionOpened()...sessionId=" + session.getId());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //TODO 清除断开连接的用户
        log.debug("sessionClosed()...sessionId=" + session.getId());
//        String userId = (String) SessionUsers.getInstance().getSession(session.getId());
//        if (StringUtils.isNotBlank(userId))
//            SessionUsers.getInstance().clearSession(userId);
        SessionUsers.getInstance().clearSession(session.getId());
        session.close(true);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.debug("sessionIdle()...sessionId=" + session.getId());
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.debug("messageSent()...sessionId=" + session.getId() + ", message="
                + ToStringBuilder.reflectionToString(message));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.debug("exceptionCaught()..." + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.debug("messageReceived()...业务消息处理" + ToStringBuilder.reflectionToString(message));
        BaseMessage baseMessage = new BaseMessage();
        try {
            baseMessage = (BaseMessage) message;
        } catch (Exception e) {
            baseMessage.setType(Constants.T101);
        }
        //业务消息处理
        ActionHandler.process(session, baseMessage);

    }
}
