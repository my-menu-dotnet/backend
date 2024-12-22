package net.mymenu.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import net.mymenu.exception.Exception;
import net.mymenu.exception.InternalErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeService {

    private final static int HEIGHT = 400;
    private final static int WIDTH = 400;

    public static byte[] generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException | Exception e) {
            throw new InternalErrorException("Could not generate QR Code");
        }
    }

    public static byte[] generateQRCodeImage(String text) {
        return generateQRCodeImage(text, WIDTH, HEIGHT);
    }
}
