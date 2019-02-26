package ru.astronarh.mosaic.controller;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        return "index.html";
        /*ImagePlus imagePlus = IJ.openImage("D:\\Проекты\\mosaic\\src\\main\\resources\\static\\img\\forest.JPG");

        ImageProcessor imageProcessor = imagePlus.getProcessor();

        System.out.println(imageProcessor.getHeight());

        System.out.println(imageProcessor.getWidth());

        imageProcessor.resize(800, 600);

        FileSaver fileSaver = new FileSaver(imagePlus);

        return fileSaver.getDescriptionString();*/
    }
}
