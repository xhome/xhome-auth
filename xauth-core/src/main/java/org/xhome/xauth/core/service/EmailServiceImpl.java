package org.xhome.xauth.core.service;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Mar 10, 2014
 * @describe
 */
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private AuthConfigService authConfigService;

	/**
	 * @throws MessagingException
	 * @see org.xhome.xauth.core.service.EmailService#sendEmail(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmail(String to, String subject, String body)
			throws MessagingException {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		String host = authConfigService.getSMTPHost(), username = authConfigService
				.getSMTPUserName(), password = authConfigService
				.getSMTPPassword(), from = authConfigService.getSMTPFrom();
		int port = authConfigService.getSMTPPort();
		boolean ssl = authConfigService.enableSMTPSSL();

		sender.setHost(host);
		sender.setUsername(username);
		sender.setPassword(password);
		sender.setPort(port);

		Properties p = new Properties();
		p.setProperty("mail.transport.protocol", "smtp");
		p.setProperty("mail.smtp.auth", "true");
		p.setProperty("mail.smtp.starttls.enable", "" + ssl);
		sender.setJavaMailProperties(p);

		sendEmail(from, to, subject, body, "UTF-8", sender);
	}

	/**
	 * @throws MessagingException
	 * @see org.xhome.xauth.core.service.EmailService#sendEmail(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, org.springframework.mail.MailSender)
	 */
	@Override
	public void sendEmail(final String from, final String to,
			final String subject, final String body, final String encoding,
			final JavaMailSender sender) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message,
				encoding);

		messageHelper.setFrom(from);
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		messageHelper.setText(body, true);

		sender.send(message);
	}

	/**
	 * @return the authConfigService
	 */
	public AuthConfigService getAuthConfigService() {
		return authConfigService;
	}

	/**
	 * @param authConfigService
	 *            the authConfigService to set
	 */
	public void setAuthConfigService(AuthConfigService authConfigService) {
		this.authConfigService = authConfigService;
	}

}
