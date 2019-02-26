package ru.astronarh.mosaic.controller;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/image")
    public String image() {
        ImagePlus imagePlus = IJ.openImage("D:\\Проекты\\mosaic\\src\\main\\resources\\static\\img\\forest.JPG");

        ImageProcessor imageProcessor = imagePlus.getProcessor();

        System.out.println(imageProcessor.getHeight());

        System.out.println(imageProcessor.getWidth());

        imageProcessor.resize(800, 600);

        FileSaver fileSaver = new FileSaver(imagePlus);

        return fileSaver.getDescriptionString();
    }
}
