package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.input.UploadFileInput;
import com.tgfcodes.upfile.application.output.FileDetailsOutput;
import com.tgfcodes.upfile.application.output.UploadFileOutput;
import com.tgfcodes.upfile.application.usecase.DeleteFileUseCase;
import com.tgfcodes.upfile.application.usecase.GetFileDetailsUseCase;
import com.tgfcodes.upfile.application.usecase.UploadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/files", version = "1.0")
public class UploadFileController {

    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

    private final UploadFileUseCase uploadFileUseCase;

    private final GetFileDetailsUseCase fileDetailsUseCase;

    private final DeleteFileUseCase deleteFileUseCase;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadFileResponse> upload(@RequestParam(value = "file") MultipartFile file) {

        if (file.isEmpty()) {
            log.error("Empty file received.");
            return ResponseEntity.badRequest().build();
        }

        UploadFileInput uploadFileInput = new UploadFileInput(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file::getInputStream
        );

        UploadFileOutput uploadFileOutput = uploadFileUseCase.execute(uploadFileInput);
        UploadFileResponse uploadFileResponse = UploadFileResponse.from(uploadFileOutput);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(uploadFileResponse.id())
                .toUri();

        log.info("File uploaded successfully");
        return ResponseEntity.created(location).body(uploadFileResponse);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FileDetailsResponse> getFileDetails(@PathVariable UUID id) {

        FileDetailsOutput fileDetailsOutput = fileDetailsUseCase.execute(id);
        FileDetailsResponse fileDetailsResponse = FileDetailsResponse.from(fileDetailsOutput);

        log.info("File details retrieved successfully");
        return ResponseEntity.ok(fileDetailsResponse);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteFileUseCase.execute(id);
        log.info("File deleted successfully");
        return ResponseEntity.noContent().build();
    }
}