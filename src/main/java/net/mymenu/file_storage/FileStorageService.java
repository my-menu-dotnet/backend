package net.mymenu.file_storage;

import com.amazonaws.services.fms.model.InternalErrorException;
import net.mymenu.exception.NotFoundException;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    public static final String UPLOAD_DIR = "src/main/resources/static/images/";
    private static final float DEFAULT_COMPRESSION_QUALITY = 0.5f;
    private static final int MAX_IMAGE_WIDTH = 1920;
    private static final int MAX_IMAGE_HEIGHT = 1080;

    public FileStorage saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File is empty");
        }

        try {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new NotFoundException("File name not found");
            }

            String contentType = file.getContentType();
            boolean isImage = contentType != null && contentType.startsWith("image/");

            return isImage ? optimizeAndSaveImage(file) : saveRegularFile(file, originalFileName);
        } catch (IOException e) {
            throw new InternalErrorException("Failed to save file");
        }
    }

    private FileStorage optimizeAndSaveImage(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        BufferedImage resizedImage = resizeImage(originalImage);

        String formatName = Objects.requireNonNull(file.getContentType()).substring(file.getContentType().lastIndexOf('/') + 1);

        byte[] data = compressImage(resizedImage, formatName);

        String fileName = UUID.randomUUID() + "." + formatName;

        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, data);

        return fileStorageRepository.save(
                FileStorage.builder()
                        .fileName(fileName)
                        .fileType(file.getContentType())
                        .size(data.length)
                        .build()
        );
    }

    private BufferedImage resizeImage(BufferedImage originalImage) {
        return Scalr.resize(
                originalImage,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.AUTOMATIC,
                MAX_IMAGE_WIDTH,
                MAX_IMAGE_HEIGHT,
                Scalr.OP_ANTIALIAS
        );
    }

    private byte[] compressImage(BufferedImage originalImage, String formatName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);

        ImageWriter writer = writers.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();

        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(DEFAULT_COMPRESSION_QUALITY);

        writer.setOutput(ImageIO.createImageOutputStream(outputStream));
        writer.write(null, new IIOImage(originalImage, null, null), writeParam);

        writer.dispose();

        return outputStream.toByteArray();
    }

    private FileStorage saveRegularFile(MultipartFile file, String originalFileName) throws IOException {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + fileExtension;
        Path path = Paths.get(UPLOAD_DIR + fileName);

        Files.createDirectories(path.getParent());
        file.transferTo(path);

        return fileStorageRepository.save(
                FileStorage.builder()
                        .fileName(fileName)
                        .fileType(file.getContentType())
                        .size(file.getSize())
                        .build()
        );
    }
}