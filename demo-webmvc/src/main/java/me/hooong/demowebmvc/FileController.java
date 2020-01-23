package me.hooong.demowebmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {

    @GetMapping("/file")
    public String fileUploadForm() {
        return "files/index";
    }

    @PostMapping("/file")
    public String fileUpload(@RequestParam MultipartFile file,
                             RedirectAttributes attributes) {
        // save
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        String message = file.getOriginalFilename() + " is uploaded";
        attributes.addFlashAttribute("message", message);
        return "redirect:/file";
    }
}
