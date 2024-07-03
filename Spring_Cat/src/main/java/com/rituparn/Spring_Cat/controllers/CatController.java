package com.rituparn.Spring_Cat.controllers;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping({"/cat", "/cat/"})
public class CatController {

    private  final ResourcePatternResolver resourcePatternResolver;
    private final Random random;
    private final AtomicInteger meowCounter= new AtomicInteger();
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
    public String meow(Model model) {
        int currentCount= meowCounter.incrementAndGet();
        model.addAttribute("catspeak", "meow");
        model.addAttribute("Counter", currentCount);
        meowcounter.increment();
        return "meow";
    }
    @GetMapping({"/show", "/show/"})
    public String catImage(Model model) throws IOException {
        String Imageurl;
        Resource [] resources=resourcePatternResolver.getResources(imageDirectory);
        Resource randomImage=resources[random.nextInt(resources.length)];
        Imageurl= String.valueOf(randomImage.getFilename());
//        System.out.println(Imageurl);
        model.addAttribute("Imageurl",Imageurl);
        return "catImage";
    }

    @GetMapping("images/show/")
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
