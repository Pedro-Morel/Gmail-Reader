package com.gmailreader;

import javax.mail.internet.MimeBodyPart;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AttachmentSaver {

    public static void saveAttachment(MimeBodyPart part) {
        try {
            InputStream is = part.getInputStream();
            byte[] buf = new byte[4096];
            int bytesRead;
            StringBuilder xmlContent = new StringBuilder();
            while ((bytesRead = is.read(buf)) != -1) {
                xmlContent.append(new String(buf, 0, bytesRead));
            }

            // Crear instancia de XMLEntity y almacenar el contenido
            XMLEntity xmlEntity = new XMLEntity();
            xmlEntity.addXMLContent(xmlContent.toString());

            // Opcional: guardar en un archivo
            FileOutputStream fos = new FileOutputStream("your-path" + part.getFileName()); // Reemplazar con la ruta deseada
            fos.write(xmlContent.toString().getBytes());
            fos.close();

            // Imprimir el contenido del XML para verificar
            System.out.println("Contenido del XML guardado: " + xmlContent.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
