package com.example.demo.line.service;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;

import java.util.List;

public interface ReplyService {
	/**
	 * 取得要回應的訊息
	 */
	List<ReplyMessage> getReplyInfo(List<Event> events);

}
