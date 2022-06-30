package net.ifonlygaram.util;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/emailSend")
public class EmailSend extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String mail_from = "MainStream<admin@MainStream.com>";
			String mail_to = req.getParameter("email");
			String title = "- MainStream - 인증번호를 입력해주세요";
			
			UUID uuid = UUID.randomUUID();
			String styleProp = "style='color: red; font-size: 16px;'>";
			String contents = "인증번호 : <br><br><strong " + styleProp + uuid.toString() + "</strong><br><br> 를 입력해주세요";
			
			mail_from = new String(mail_from.getBytes("UTF-8"), "UTF-8");
			mail_to = new String(mail_to.getBytes("UTF-8"), "UTF-8");
			
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", 465);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			// SMTP 서버에 접속하기 위한 정보들
			
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);
			
//			session.setDebug(true);
			
			// 메일의 내용을 담을 객체
			MimeMessage msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(mail_from));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail_to));
			msg.setSubject(title, "UTF-8");
			msg.setContent(contents, "text/html; charset=UTF-8");
			msg.setHeader("Content-type", "text/html; charset=UTF-8");
			
			Transport.send(msg);
			resp.getWriter().print(uuid.toString());
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("실패,,, 연결은 되는듯?");
		}
	}
}
