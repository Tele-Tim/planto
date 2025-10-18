package planto_project.imagekit.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import planto_project.dto.FileDto;
import planto_project.imagekit.dto.UploadImageAnswerDto;
import planto_project.imagekit.model.ImageKitProperties;

@Service

@Slf4j
public class ImageKitServiceImpl implements ImageKitService {

    private final ImageKit imageKit;

    public ImageKitServiceImpl(ImageKitProperties imgkitProperties) {
        Configuration config = new Configuration(
                imgkitProperties.getPublicKey(),
                imgkitProperties.getPrivateKey(),
                imgkitProperties.getUrlEndpoint()
        );
        this.imageKit = ImageKit.getInstance();
        this.imageKit.setConfig(config);
        log.info("Init ImageKit with url={}, pubKey={}, privKey exists? {}",
                imgkitProperties.getUrlEndpoint(),
                imgkitProperties.getPublicKey(),
                imgkitProperties.getPrivateKey() != null);
    }

    @Override
    public UploadImageAnswerDto uploadImage(FileDto fileDto) {
        if (fileDto.getFile() == null || fileDto.getFileName() == null || fileDto.getFileName().isBlank()) {
            return new UploadImageAnswerDto(null, false, "Invalid file data");
        }

        try {
            final byte[] imageBytes = fileDto.getFile().getBytes();
            FileCreateRequest fileCreate = new FileCreateRequest(imageBytes, fileDto.getFileName());
            Result result = imageKit.upload(fileCreate);

            log.info("Image [{}] uploaded successfully: {}", fileDto.getFileName(), result.getUrl());
            return new UploadImageAnswerDto(result.getUrl(), true, null);
        } catch (Exception e) {
            log.error("Error uploading image [{}]", fileDto.getFileName(), e);
            return new UploadImageAnswerDto(null, false, "Failed to upload image");
        }
    }
}
