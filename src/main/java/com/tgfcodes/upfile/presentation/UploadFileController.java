package com.tgfcodes.upfile.presentation;

import com.tgfcodes.upfile.application.UploadFileCommand;
import com.tgfcodes.upfile.application.UploadFileOutput;
import com.tgfcodes.upfile.application.UploadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/files", version = "1.0")
public class UploadFileController {

    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

    private final UploadFileUseCase uploadFileUseCase;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadFileResponse> upload(@RequestParam(value = "file") MultipartFile file) {

        if (file.isEmpty()) {
            log.error("Empty file received.");
            return ResponseEntity.badRequest().build();
        }

        UploadFileCommand uploadFileCommand = new UploadFileCommand(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file::getInputStream
        );

        UploadFileOutput uploadFileOutput = uploadFileUseCase.execute(uploadFileCommand);
        UploadFileResponse uploadFileResponse = UploadFileResponse.from(uploadFileOutput);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(uploadFileResponse.id())
                .toUri();

        log.info("File uploaded successfully");
        return ResponseEntity.created(location).body(uploadFileResponse);
    }
}