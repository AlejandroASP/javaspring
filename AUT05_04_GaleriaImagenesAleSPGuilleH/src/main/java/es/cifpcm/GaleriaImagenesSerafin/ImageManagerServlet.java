package es.cifpcm.GaleriaImagenesSerafin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageManagerServlet {

    private String UPLOAD_FOLDER = System.getProperty("user.dir") + "/target/uploadsSerafin";
    @PostMapping("/imageManager")
    public String upload(@RequestParam("imageFile") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            // Comprueba el tamaño del archivo
            long sizeInBytes = file.getSize();
            long sizeInMb = sizeInBytes / (1024 * 1024);
            if (sizeInMb > 10) { // Cambia este valor al límite de tamaño que desees
                redirectAttributes.addFlashAttribute("message", "El archivo es demasiado grande. Por favor, sube un archivo de menos de 10 MB.");
                return "redirect:/index";
            }

            File directory = new File(UPLOAD_FOLDER);
            byte[] bytes = file.getBytes();
            if (!directory.exists()) {
                directory.mkdir();
            }
            Path path = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());
            Files.write(path, bytes);
            redirectAttributes.addFlashAttribute("message", "Archivo subido correctamente");
            return "redirect:/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error al subir el archivo: " + e.getMessage());
            return "redirect:/index";
        }
    }

    @GetMapping({"/imageManager", "/index"})
    public String listImages(Model model) {
        File folder = new File(UPLOAD_FOLDER);
        File[] listOfFiles = folder.listFiles();
        List<String> imageLinks = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                imageLinks.add("/uploadsSerafin/" + file.getName());
            }
        }
        model.addAttribute("imageLinks", imageLinks);
        return "index";
    }
}
