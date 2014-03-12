package org.xhome.xauth.core.service;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.JavaMailSender;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Mar 10, 2014
 * @describe
 */
public interface EmailService {

	/**
	 * 邮件发送（默认邮件内容使用UTF-8字符集）
	 * 
	 * @param to
	 *            邮件发送地址
	 * @param subject
	 *            邮件主题
	 * @param body
	 *            邮件内容
	 * @throws MessagingException
	 */
	public void sendEmail(String to, String subject, String body)
			throws MessagingException;

	/**
	 * 邮件发送
	 * 
	 * @param from
	 *            邮件发送源地址
	 * @param to
	 *            邮件发送目标地址
	 * @param subject
	 *            邮件主题
	 * @param body
	 *            邮件内容
	 * @param encoding
	 *            邮件内容字符集
	 * @param sender
	 *            邮件发送器
	 * @throws MessagingException
	 */
	public void sendEmail(String from, String to, String subject, String body,
			String encoding, JavaMailSender sender) throws MessagingException;

}