/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassesAuxiliares;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Luciano
 */
public class mailSender {
    
    private String email = "yourmail@gmail";
    
    public void sendMail(String titulo, String mensagem, String destinatariosSeparadosPorVirgula) {
            Properties props = new Properties();
            /** Parâmetros de conexão com servidor Gmail */
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                             protected PasswordAuthentication getPasswordAuthentication() 
                             {
                                   return new PasswordAuthentication("yourmail@gmail.com", "yourpassword");
                             }
                        });
            /** Ativa Debug para sessão */  //session.setDebug(true);
            
            try {

                  Message message = new MimeMessage(session);
                  message.setFrom(new InternetAddress(this.email)); //Remetente

                  Address[] toUser = InternetAddress //Destinatário(s)
                             .parse(destinatariosSeparadosPorVirgula);  

                  message.setRecipients(Message.RecipientType.TO, toUser);
                  message.setSubject(titulo);//Assunto
                  message.setText(mensagem);
                  /**Método para enviar a mensagem criada*/
                  Transport.send(message);

                  System.out.println("E-mail enviado com sucesso!");

             } catch (MessagingException e) {
                  throw new RuntimeException(e);
            }
      }
    
}
