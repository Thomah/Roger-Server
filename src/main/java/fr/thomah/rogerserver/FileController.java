package fr.thomah.rogerserver;

import fr.thomah.rogerserver.entities.FileData;
import fr.thomah.rogerserver.exceptions.ForbiddenException;
import fr.thomah.rogerserver.exceptions.InternalServerException;
import fr.thomah.rogerserver.exceptions.NotFoundException;
import fr.thomah.rogerserver.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class FileController {

    private static final File FILES_DIR = new File("./files");
    private static String TOKEN;

    @Autowired
    private Environment env;

    @Autowired
    private FileDataRepository fileDataRepository;

    @PostConstruct
    public void init() {
        TOKEN = env.getProperty("ROGER_TOKEN");
        if(TOKEN == null) {
            throw new IllegalArgumentException("ROGER_TOKEN is not set");
        }
        if (!FILES_DIR.exists()) {
            if(!FILES_DIR.mkdirs()) {
                throw new RuntimeException("Error creating files directory");
            }
        }
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FileData> getFileList(@RequestParam("isSync") boolean isSync) {
        return fileDataRepository.findByIsSync(isSync);
    }

    @RequestMapping(value = "/files/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FileData upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path filepath = Paths.get(FILES_DIR.getPath(), fileName);
        FileData fileData = null;
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
            fileData = fileDataRepository.save(new FileData(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public FileSystemResource get(@PathVariable("id") String id, @RequestParam("token") String token) {
        if(TOKEN.equals(token)) {
            Optional<FileData> fileDataOpt = fileDataRepository.findById(id);
            if(fileDataOpt.isPresent()) {
                FileData fileData = fileDataOpt.get();
                Path p = Paths.get(FILES_DIR.getPath(), fileData.fileName);
                if(p.toFile().exists()) {
                    return new FileSystemResource(Paths.get(FILES_DIR.getPath(), fileData.fileName));
                } else {
                    throw new NotFoundException();
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new ForbiddenException();
        }
    }

    @RequestMapping(value = "/files/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String delete(@PathVariable("id") String id, @RequestParam("token") String token) {
        if(TOKEN.equals(token)) {
            Optional<FileData> fileDataOpt = fileDataRepository.findById(id);
            if(fileDataOpt.isPresent()) {
                FileData fileData = fileDataOpt.get();
                File f = Paths.get(FILES_DIR.getPath(), fileData.fileName).toFile();
                if (f.exists()) {
                    if (f.delete()) {
                        fileDataRepository.delete(fileData);
                        return null;
                    } else {
                        throw new InternalServerException();
                    }
                } else {
                    throw new NotFoundException();
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new ForbiddenException();
        }
    }
}
