package com.alumni.career.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(String to, String firstName) {
        String subject = "Welcome to Alumni Career Portal";
        String body = String.format(
            "Dear %s,\n\n" +
            "Welcome to the Alumni Career Management System!\n\n" +
            "You can now access exclusive job opportunities, connect with fellow alumni, " +
            "and attend career development events.\n\n" +
            "Log in to your dashboard to get started: http://localhost:8080\n\n" +
            "Best regards,\n" +
            "Alumni Career Portal Team",
            firstName
        );
        
        sendEmail(to, subject, body);
    }

    public void sendJobApplicationConfirmation(String to, String jobTitle, String companyName) {
        String subject = "Application Received - " + jobTitle;
        String body = String.format(
            "Your application for %s at %s has been received.\n\n" +
            "We will notify you of any updates regarding your application.\n\n" +
            "Best of luck!\n" +
            "Alumni Career Portal Team",
            jobTitle,
            companyName
        );
        
        sendEmail(to, subject, body);
    }

    public void sendEventReminder(String to, String eventName, String eventDate) {
        String subject = "Reminder: " + eventName;
        String body = String.format(
            "This is a reminder that you're registered for:\n\n" +
            "Event: %s\n" +
            "Date: %s\n\n" +
            "We look forward to seeing you there!\n\n" +
            "Best regards,\n" +
            "Alumni Career Portal Team",
            eventName,
            eventDate
        );
        
        sendEmail(to, subject, body);
    }

    public void sendNewJobNotification(String to, String jobTitle, String companyName) {
        String subject = "New Job Posting: " + jobTitle;
        String body = String.format(
            "A new job opportunity has been posted that might interest you:\n\n" +
            "Position: %s\n" +
            "Company: %s\n\n" +
            "Log in to view details and apply: http://localhost:8080\n\n" +
            "Best regards,\n" +
            "Alumni Career Portal Team",
            jobTitle,
            companyName
        );
        
        sendEmail(to, subject, body);
    }
}
