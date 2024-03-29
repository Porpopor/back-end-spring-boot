package BackEnd.BackEnd.controller;

import BackEnd.BackEnd.entity.UsersEntity;
import BackEnd.BackEnd.repository.UsersRepository;
import BackEnd.BackEnd.service.TokenService;
import BackEnd.BackEnd.exception.BaseException;
import BackEnd.BackEnd.exception.UsersException;
import BackEnd.BackEnd.model.Response;
import BackEnd.BackEnd.model.UploadFileReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class FileController {

    UploadFileReq url = new UploadFileReq();
    public static String uploadDirectory = System.getProperty("user.dir");
    public final TokenService tokenService;
    private final UsersRepository usersRepository;

    public FileController(TokenService tokenService, UsersRepository usersRepository) {
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
    }


    @PostMapping("/image/user-profile")
    public Object uploadProfilePicture(@RequestParam("fileName") MultipartFile file ) throws IOException {

        UsersEntity user = tokenService.getUserByIdToken();
        System.out.println(file.getOriginalFilename());
        String dir = new UploadFileReq().getDirUserProfile();
        String timeStamp = new UploadFileReq().getTimeStamp();
        String imgName = new UploadFileReq().getImgName();

        if (file == null) {
            //throw error
            throw UsersException.userAlreadyExists();
        }
        if (file.getSize() > 1048576 * 5) {
//            throw error
            throw UsersException.userAlreadyExists();
        }
        String contentType = file.getContentType();
        if (contentType == null) {
            //throw  error
            throw UsersException.userAlreadyExists();
        }

        StringBuilder fileNames = new StringBuilder();

        Path fileNameAndPath = Paths.get(uploadDirectory + dir, imgName);
        fileNames.append(file.getOriginalFilename());
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Object, Object> img = new HashMap<>();
        img.put("url", "http://localhost:8080" + dir + imgName);
        img.put("name", imgName);
        user.setPicture(imgName);
        usersRepository.save(user);

//        return new Response().success("yeah");
        return new Response().ok("upload success", "img", img);

    }


    @GetMapping("/userProfile")
    public ResponseEntity<Object>getUserProfile() throws BaseException {
        UsersEntity usertoken = tokenService.getUserByIdToken();
        Optional<UsersEntity> usersEntity = usersRepository.findById(usertoken.getId());
        UsersEntity user = usersEntity.get();
        user.setPicture(url.getHost() + url.getDirUserProfile() + user.getPicture());
        return ResponseEntity.ok(user.getPicture());
    }


}
