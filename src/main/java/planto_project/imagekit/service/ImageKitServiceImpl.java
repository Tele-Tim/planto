package planto_project.imagekit.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planto_project.dto.FileDto;
import planto_project.imagekit.dto.UploadImageAnswerDto;
import planto_project.imagekit.model.ImageKitProperties;

@Service
@RequiredArgsConstructor
public class ImageKitServiceImpl implements ImageKitService {

    final ImageKitProperties imgkitProperties;

    @Override
    public UploadImageAnswerDto uploadImage(FileDto fileDto) {

        String publicKey = imgkitProperties.getPublicKey();
        String privateKey = imgkitProperties.getPrivateKey();
        String url = imgkitProperties.getUrlEndpoint();

        ImageKit imageKit = ImageKit.getInstance();
        Configuration config = new Configuration(publicKey, privateKey, url);
        imageKit.setConfig(config);

        byte[] imageBytes = null;
        try {
            imageBytes = fileDto.getFile().getBytes();
            FileCreateRequest fileCreate = new FileCreateRequest(imageBytes, fileDto.getFileName());
            Result result = ImageKit.getInstance().upload(fileCreate);
            return new UploadImageAnswerDto(result.getUrl(), "");
        } catch (Exception e) {
            String msg = e.getMessage();
            return new UploadImageAnswerDto("", e.getMessage());
        }
    }
}
