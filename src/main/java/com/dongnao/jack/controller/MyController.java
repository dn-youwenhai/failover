package com.dongnao.jack.controller;

import java.util.Hashtable;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dongnao.jack.dao.IMongoUserDao;

@Controller
@RequestMapping("/my")
public class MyController {

	@Autowired
	IMongoUserDao dao;

	@Autowired
	@Qualifier("mongoTemplate1")
	MongoTemplate mt;
	
	@RequestMapping("/upload")
	public @ResponseBody String upload() {
		
		return dao.upload(null);
	}
	
	@RequestMapping("/download")
	public @ResponseBody String download() {
		
		return dao.download(null);
	}

	@RequestMapping("/insert")
	public @ResponseBody String insert() {

		return dao.insert(null);
	}

	@RequestMapping("/sum")
	public @ResponseBody String sum() {

		try {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		props.put(Context.PROVIDER_URL, "localhost:8080");
		props.put("java.naming.rmi.security.manager", "yes");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");
		Context context = new InitialContext();

		QueueConnectionFactory qcf = (QueueConnectionFactory) context
				.lookup("queueConnectionFactory");

		QueueConnection qc = qcf.createQueueConnection();

		QueueSession qs = qc.createQueueSession(false, 1);

		Queue queue = (Queue) context.lookup("queue");

		QueueSender qsder = qs.createSender(queue);

		TextMessage tm = qs.createTextMessage();

		for (int i = 0; i < 100; i++) {

			tm.setText("msg" + i);
		}

		qsder.send(queue,tm);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "OK";
	
	}
}
