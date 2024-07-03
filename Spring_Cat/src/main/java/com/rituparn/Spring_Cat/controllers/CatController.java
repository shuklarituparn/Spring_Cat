package com.rituparn.Spring_Cat.controllers;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/cat")
public class CatController {
    private  final ResourcePatternResolver resourcePatternResolver;
    private final Random random;

    MeterRegistry meterRegistry;

    Counter meowcounter;
    Counter catPicCounter;

    @Value("${image.directory:classpath:/static/images/*.jpg}")
    private String imageDirectory;

    public CatController(ResourcePatternResolver resourcePatternResolver, MeterRegistry  meterRegistry){
        this.resourcePatternResolver=resourcePatternResolver;
        this.random=new Random();
        this.meterRegistry=meterRegistry;
        this.meowcounter=Counter.builder("meow_metric").description("Metric to check meows").register(meterRegistry);
        this.catPicCounter=Counter.builder("cat_pics").description("Metric to show the cat pics").register(meterRegistry);

    }

    @GetMapping
    public String meow(){
        meowcounter.increment();
        return "Meow!";

    }

    @GetMapping("/show/")
    public ResponseEntity<Resource> show_image(){
        try{

            Resource [] resources=resourcePatternResolver.getResources(imageDirectory);
            if(resources.length==0){
                return ResponseEntity.notFound().build();
            }
            Resource randomImage= resources[random.nextInt(resources.length)];
            catPicCounter.increment();
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(randomImage);
        }catch (IOException e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
