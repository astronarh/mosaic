package ru.astronarh.mosaic.controller;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/image")
    public void uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "brightness", defaultValue = "0") float brightness, RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

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

        imageRepository.save(new ImageEntity(UUID.randomUUID().getLeastSignificantBits(), file.getName(), file.getContentType(), imageInByte));

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
}
