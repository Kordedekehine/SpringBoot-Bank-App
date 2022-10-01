package koredebank.example.bank.cloudinaryService;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudStorageService {

    Map<?,?> uploadImage(File file, Map<?,?> imageProperties) throws IOException;

    Map<?,?> uploadImage(MultipartFile multipartFile, Map<?, ?> imageProperties) throws IOException;
}
