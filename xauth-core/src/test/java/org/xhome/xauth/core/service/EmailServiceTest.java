package org.xhome.xauth.core.service;

import javax.mail.MessagingException;

import org.junit.Test;
import org.xhome.xauth.core.AbstractTest;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Mar 10, 2014
 * @describe
 */
public class EmailServiceTest extends AbstractTest {

	private EmailService emailService;

	public EmailServiceTest() {
		emailService = context.getBean(EmailServiceImpl.class);
	}

	@Test
	public void test() {
		try {
			emailService
					.sendEmail(
							"cpf624@126.com",
							"Email Service Test0",
							"<h1>Hello boy!<h1><div style='color: red;'>system email, please don't reply!</div>");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
