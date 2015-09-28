package com.example.peterk2.driversurvey;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by peterk2 on 10/31/2014.
 */
public class emailResults {
    private String LOG_TAG ="emailResults";
    public emailResults(){
    }

    public void sendEmail(Context context, String email, String surveyName){
        Log.i(LOG_TAG, "In emailResults function...");
        //create new mail message
        Session session = createSessionObject();
        Multipart multipart = new MimeMultipart();
        Log.i(LOG_TAG,"Session created...");
        //get current date and put it into string format
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String dateString = df.format(date);

        String subject =  surveyName+" Survey Results";
        String messageBody = "Here are the survey results.\nSent by Drive Survey app on "+ dateString+".";
        String filename = "results.csv";
        String path = "/storage/sdcard0/Android/data/com.example.peterk2.driversurvey/files/Test Survey/results.csv";
        //create parts of message Body
        BodyPart msgTextBodyPart = new MimeBodyPart();
        BodyPart messageBodyPart = new MimeBodyPart();
        Random r = new Random();
        String rand = String.valueOf(r.nextInt(1000));
        Log.i(LOG_TAG,"Parts created, entering try...");
        try {
            //attach results file to email message
            DataSource source = new FileDataSource(path);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(rand+filename);
            multipart.addBodyPart(messageBodyPart);
            //set email body text
            msgTextBodyPart.setText(messageBody);
            multipart.addBodyPart(msgTextBodyPart);
            //create email message
            Message message = createMessage(email, subject, session);
            //attach multipart body
            message.setContent(multipart);
            Log.i(LOG_TAG,"About to send mail");

            new SendMailTask().execute(message);
            Log.i(LOG_TAG,"Hopefully Sent mail");
        } catch (AddressException e) {
            Log.i(LOG_TAG,"Exception, AddressException caught");
            e.printStackTrace();
        } catch (MessagingException e) {
            Log.i(LOG_TAG,"Exception, MessagingException caught");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.i(LOG_TAG,"Exception, UnsupportedCodingException caught");
            e.printStackTrace();
        }
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("nissancatablet@gmail.com", "Pathfinder1");
            }
        });
    }
    private Message createMessage(String email, String subject, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("nissancatablet@gmail.com", "Nissan DriverSurvey"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        return message;
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(LOG_TAG, "Sending Mail");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
