package koredebank.example.bank.cloudinaryService;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageServiceImpl implements CloudStorageService{

    @Autowired
    Cloudinary cloudinary;

    @Override
    public Map<?, ?> uploadImage(File file, Map<?, ?> imageProperties) throws IOException {
        return cloudinary.uploader().upload(file,imageProperties);
    }

    @Override
    public Map<?, ?> uploadImage(MultipartFile multipartFile, Map<?, ?> imageProperties) throws IOException {
        return cloudinary.uploader().upload(multipartFile.getBytes(),imageProperties);
    }
}
