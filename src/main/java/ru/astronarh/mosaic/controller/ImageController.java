package ru.astronarh.mosaic.controller;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.astronarh.mosaic.jpa.ImageRepository;
import ru.astronarh.mosaic.model.ImageEntity;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ImageController {

    private AtomicLong imageId = new AtomicLong();

    private float brightness;

    @Autowired
    private ImageRepository imageRepository;

    private Path tmpFile;

    private Path tmpOriginalFile;

    @PostMapping("/image")
    public void uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "brightness", defaultValue = "1") float brightness, RedirectAttributes redirectAttributes, HttpServletResponse response) throws Exception {

        String tmpPath = System.getProperty("java.io.tmpdir");

        tmpOriginalFile = Paths.get(tmpPath + file.getOriginalFilename());

        Files.write(tmpOriginalFile, file.getBytes());

        tmpFile = Paths.get(tmpPath + "notOriginal" + file.getOriginalFilename());

        Files.write(tmpFile, file.getBytes());

//        image = new ImageEntity(UUID.randomUUID().getLeastSignificantBits(), file.getName(), file.getContentType(), file.getBytes());

        /*BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        int[] pixel = { 0, 0, 0, 0 };
        float[] hsbvals = { 0, 0, 0 };

        for ( int i = 0; i < bufferedImage.getHeight(); i++ ) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                // get the pixel data
                bufferedImage.getRaster().getPixel( j, i, pixel );

                // converts its data to hsb to change brightness
                Color.RGBtoHSB( pixel[0], pixel[1], pixel[2], hsbvals );

                // create a new color with the changed brightness
                Color c = new Color( Color.HSBtoRGB( hsbvals[0], hsbvals[1], hsbvals[2] * brightness ) );

                // set the new pixel
                bufferedImage.getRaster().setPixel( j, i, new int[]{ c.getRed(), c.getGreen(), c.getBlue(), pixel[3] } );
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bufferedImage, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        imageRepository.save(new ImageEntity(UUID.randomUUID().getLeastSignificantBits(), file.getName(), file.getContentType(), imageInByte));*/

//        ByteProcessor byteProcessor = new ByteProcessor(100, 100, file.getBytes());

//        return  "redirect:/index.html";

        /*ImagePlus imagePlus = IJ.openImage("D:\\Проекты\\mosaic\\src\\main\\resources\\static\\img\\forest.JPG");

        ImageProcessor imageProcessor = imagePlus.getProcessor();

        System.out.println(imageProcessor.getHeight());

        System.out.println(imageProcessor.getWidth());

        imageProcessor.resize(800, 600);

        FileSaver fileSaver = new FileSaver(imagePlus);

        return fileSaver.getDescriptionString();*/

        response.sendRedirect("/");
    }

    @GetMapping("/list")
    public List<ImageEntity> list(){
        return imageRepository.findAll();
    }

    @PostMapping("/getImage")
    public ImageEntity getImage() throws IOException {
        //return imageRepository.findAll().get(0);
        //return image;

        String name = tmpFile.getFileName().toString();

        if( name == null ) return null;

        long id = imageId.incrementAndGet();

        String type = FilenameUtils.getExtension(tmpFile.getFileName().toString());

        byte[] pic = Files.readAllBytes(tmpFile);

        ImageEntity imageEntity = new ImageEntity(id, name, type, pic);

        return imageEntity;
    }

    @PostMapping("/updateImage")
    public void updateImage(@RequestParam("brightness") float brightness) throws IOException {

        //image.setPic(Base64.getEncoder().encode(imageString.split(";base64,")[1].getBytes()));

        changeBrightness(brightness);

        System.out.println(brightness);
    }

    @PostMapping("/getPixelatedImage")
    public void pixelatedImage() {

    }

    private void changeBrightness(float brightness) throws IOException {

        byte[] pic = Files.readAllBytes(tmpOriginalFile);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(pic));

        RescaleOp rescaleOp = new RescaleOp(brightness, 0, null);

        bufferedImage = rescaleOp.filter(bufferedImage, bufferedImage);

        /*int[] pixel = { 0, 0, 0, 0 };
        float[] hsbvals = { 0, 0, 0 };

        for ( int i = 0; i < bufferedImage.getHeight(); i++ ) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                // get the pixel data
                bufferedImage.getRaster().getPixel( j, i, pixel );

                // converts its data to hsb to change brightness
                Color.RGBtoHSB( pixel[0], pixel[1], pixel[2], hsbvals );

                // create a new color with the changed brightness
                Color c = new Color( Color.HSBtoRGB( hsbvals[0], hsbvals[1], hsbvals[2] * brightness ) );

                // set the new pixel
                bufferedImage.getRaster().setPixel( j, i, new int[]{ c.getRed(), c.getGreen(), c.getBlue(), pixel[3] } );
            }
        }*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(resize(bufferedImage, 100, 100), "jpg", baos);

        baos.flush();

        pic = baos.toByteArray();

        baos.close();

        Files.write(tmpFile, pic);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException {
        return Thumbnails.of(img).size(newW, newH).asBufferedImage();
    }
}
