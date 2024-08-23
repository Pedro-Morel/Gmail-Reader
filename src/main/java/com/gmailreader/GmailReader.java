package com.gmailreader;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class GmailReader {

    public static void main(String[] args) {
        SSLUtil.disableSslVerification();

        String host = "imap.gmail.com";
        String username = "your-mail@gmail.com"; // Reemplazar con su correo electrónico
        String password = "your-password"; // Reemplazar con su contraseña

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore();
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            boolean xmlFound = false;
            System.out.println("Iniciando la búsqueda de archivos XML adjuntos...");
            for (Message message : messages) {
                if (message.getContent() instanceof MimeMultipart) {
                    MimeMultipart multipart = (MimeMultipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && part.getFileName().endsWith(".xml")) {
                            System.out.println("Archivo XML encontrado: " + part.getFileName());
                            AttachmentSaver.saveAttachment(part);
                            xmlFound = true;
                        }
                    }
                }
            }

            if (!xmlFound) {
                System.out.println("No se encontraron archivos XML adjuntos en los correos electrónicos.");
            } else {
                System.out.println("Se encontraron y guardaron archivos XML adjuntos.");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            System.out.println("Se produjo un error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
