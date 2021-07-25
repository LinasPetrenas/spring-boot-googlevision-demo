package com.example.demo.service.impl;

import com.example.demo.service.ImageRecognitionService;
import com.example.demo.domain.Image;
import com.example.demo.repository.ImageRecognitionRepository;
import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.NormalizedVertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author lpetrenas
 */
@Service
class ImageRecognitionServiceImpl implements ImageRecognitionService {

    /**
     * Integer array representing Hex color codes.
     */
    public static final Integer[] hexColors = {
            0x0000FF, // light blue
            0xEA9999, // pink
            0x0B5394, // dark blue
            0xFF0000, // red
            0x00FF00, // green
            0xFF9900, // orange
            0xFF00FF, // purple
            0x6F1548,
            0x074632
    };

    @Autowired
    private ImageRecognitionRepository repository;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;


    /**
     * @author lpetrenas
     */
    public ResponseEntity<Image> processImage(MultipartFile file) throws Exception {
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(
                file.getResource(), Feature.Type.OBJECT_LOCALIZATION);
        // Processed image with drawn overlay bounding boxes
        byte[] processedImage = createImageContainingProcessedObjects(file.getInputStream(), response);
        // Save to db
        Image savedImage = repository.save(createAndFillImageData(file, processedImage));
        return new ResponseEntity<>(savedImage, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     *
     * @author lpetrenas
     */
    public List<Image> findTop5ByOrderByIdDesc() {
        return repository.findTop5ByOrderByIdDesc();
    }

    /**
     * Creates new image file with recognized objects and accordingly drawn bounding boxes.
     *
     * @author lpertenas
     */
    private byte[] createImageContainingProcessedObjects(InputStream inputImage, AnnotateImageResponse response) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedImage img = ImageIO.read(inputImage);
            drawBoundingBoxes(img, response);
            ImageIO.write(img, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            bos.close();
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * Draws overlay bounding boxes to image.
     *
     * @author lpetrenas
     */
    private void drawBoundingBoxes(BufferedImage bufferedImage, AnnotateImageResponse response) {
        int index = 0;
        for (LocalizedObjectAnnotation object : response.getLocalizedObjectAnnotationsList()) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            Graphics2D graphics2D = bufferedImage.createGraphics();

            Polygon poly = new Polygon();
            for (NormalizedVertex vertex : object.getBoundingPoly().getNormalizedVerticesList()) {
                poly.addPoint((int) (vertex.getX() * width), (int) (vertex.getY() * height));
            }
            graphics2D.setStroke(new BasicStroke(2));
            // reset index if loop count exceeds hex color array
            if (index >= hexColors.length - 1) {
                index = 0;
            }
            int colorHex = hexColors[index];
            graphics2D.setColor(new Color(colorHex));
            graphics2D.draw(poly);
            drawCenteredString(graphics2D, object.getName(), poly.getBounds());
            graphics2D.dispose();
            index++;
        }
    }

    /**
     * Calculates max fitting font size for a given rectangle(bounding box).
     */
    private static int getMaxFittingFontSize(Graphics g, Font font, String text, int width, int height) {
        int minSize = 0;
        int maxSize = 288;
        int curSize = font.getSize();
        while (maxSize - minSize > 2) {
            FontMetrics fm = g.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
            int fontWidth = fm.stringWidth(text);
            int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();

            if ((fontWidth > width) || (fontHeight > height)) {
                maxSize = curSize;
                curSize = (maxSize + minSize) / 2;
            } else {
                minSize = curSize;
                curSize = (minSize + maxSize) / 2;
            }
        }
        return curSize;
    }

    /**
     * Draw a String centered in the middle of a Rectangle (Bounding box).
     *
     * @param graphics2D The Graphics instance.
     * @param text       The String to draw.
     * @param rect       The Rectangle(Bounding box) to center the text in.
     */
    private void drawCenteredString(Graphics graphics2D, String text, Rectangle rect) {
        Font font = new Font(Font.SERIF, Font.BOLD, 20);
        // Calculates max fitting font size
        int maxFont = getMaxFittingFontSize(graphics2D, font, text, rect.width, rect.height);
        Font newFont = new Font(Font.SERIF, Font.BOLD, maxFont);
        // Get the FontMetrics
        FontMetrics metrics = graphics2D.getFontMetrics(newFont);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        graphics2D.setFont(newFont);
        // Draw the String
        graphics2D.drawString(text, x, y);
    }

    /**
     * Creates and prefills {@link Image} object.
     *
     * @param file
     * @param processedImage
     * @return instantiated and prefilled {@link Image} object
     * @author lpetrenas
     */
    private Image createAndFillImageData(MultipartFile file, byte[] processedImage) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setImageData(processedImage);
        image.setType(file.getContentType());
        return image;
    }


}