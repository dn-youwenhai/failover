package com.dongnao.jack;

import java.util.Hashtable;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JMSTest {
	public void test() throws Exception {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		props.put(Context.PROVIDER_URL, "localhost:8080");
		props.put("java.naming.rmi.security.manager", "yes");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");
		Context context = new InitialContext(props);

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
	}
	
	public static void main(String[] args) {
		try {
			new JMSTest().test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
