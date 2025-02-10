package net.mymenu.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import net.mymenu.exception.MyMenuException;
import net.mymenu.exception.InternalErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    private final static int SIZE = 400;

    public static BufferedImage generateQRCode(String text, int size, String logoPath) throws Exception {
        // Configuração do QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1); // Reduz a margem branca

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size, hints);

        // Criar a imagem do QR Code
        BufferedImage qrImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = qrImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size, size);
        graphics.setColor(Color.BLACK);

        // Desenhar os pixels do QR Code
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        // Adicionar bordas arredondadas
        makeRoundedCorners(qrImage, 30);

        // Adicionar logo no centro
        if (logoPath != null) {
            addLogo(qrImage, logoPath);
        }

        graphics.dispose();
        return qrImage;
    }

    private static void addLogo(BufferedImage qrImage, String logoPath) throws IOException {
        BufferedImage logo = ImageIO.read(new File(logoPath));
        int width = qrImage.getWidth() / 5;
        int height = qrImage.getHeight() / 5;
        int x = (qrImage.getWidth() - width) / 2;
        int y = (qrImage.getHeight() - height) / 2;

        Graphics2D graphics = qrImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(logo, x, y, width, height, null);
        graphics.dispose();
    }

    private static void makeRoundedCorners(BufferedImage image, int cornerRadius) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rounded = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rounded.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        image.getGraphics().drawImage(rounded, 0, 0, null);
    }

    public static byte[] generateQRCodeImageBytes(String text, int size, String logoPath) throws Exception {
        BufferedImage qrImage = generateQRCode(text, size, logoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        return baos.toByteArray();
    }

    public static byte[] generateQRCodeImageBytes(String text, String imageName) throws Exception {
        String imagePath = FileStorageService.UPLOAD_DIR + imageName;
        return generateQRCodeImageBytes(text, SIZE, imagePath);
    }
}
