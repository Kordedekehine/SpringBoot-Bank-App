package koredebank.example.bank;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import koredebank.example.bank.cloudinaryService.CloudinaryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryConfiguration {

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @Bean
    public Cloudinary getCloudinaryConfig(){
        return new Cloudinary(ObjectUtils.asMap("cloud_name",cloudinaryConfig.getCloudName(),
                "api_key",cloudinaryConfig.getApikey(),"api_secret",cloudinaryConfig.getSecretKey()));
    }
}
