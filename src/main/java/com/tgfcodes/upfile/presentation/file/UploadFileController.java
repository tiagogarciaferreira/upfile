package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.SearchFilesFilter;
import com.tgfcodes.upfile.application.SearchFilesUseCase;
import com.tgfcodes.upfile.application.input.UploadFileInput;
import com.tgfcodes.upfile.application.output.FileDetailsOutput;
import com.tgfcodes.upfile.application.output.UploadFileOutput;
import com.tgfcodes.upfile.application.query.FileMetadataOutput;
import com.tgfcodes.upfile.application.query.PageResultOutput;
import com.tgfcodes.upfile.application.usecase.DeleteFileUseCase;
import com.tgfcodes.upfile.application.usecase.GetFileDetailsUseCase;
import com.tgfcodes.upfile.application.usecase.UploadFileUseCase;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/files", version = "1.0")
public class UploadFileController implements FilesApi {

    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

    private final UploadFileUseCase uploadFileUseCase;

    private final GetFileDetailsUseCase fileDetailsUseCase;

    private final DeleteFileUseCase deleteFileUseCase;

    private final SearchFilesUseCase searchFilesUseCase;

    @Override
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadFileResponse> upload(@RequestParam(value = "file") MultipartFile file) {

        if (file.isEmpty()) {
            log.error("Empty file received.");
            throw new DomainValidationException("File cannot be empty");
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

    @Override
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FileDetailsResponse> details(@PathVariable UUID id) {

        FileDetailsOutput fileDetailsOutput = fileDetailsUseCase.execute(id);
        FileDetailsResponse fileDetailsResponse = FileDetailsResponse.from(fileDetailsOutput);

        log.info("File details retrieved successfully");
        return ResponseEntity.ok(fileDetailsResponse);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteFileUseCase.execute(id);
        log.info("File deleted successfully");
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<FileMetadata>> search(@Valid @ModelAttribute SearchFileRequest searchFileRequest,
                                                             @RequestParam(defaultValue = "0") @Min(0) @Max(999) int pageNumber,
                                                             @RequestParam(defaultValue = "10") @Min(1) @Max(100) int pageSize,
                                                             @RequestParam(defaultValue = "type,asc") String sort) {

        SearchFilesFilter searchFilesFilter = new SearchFilesFilter(
                searchFileRequest.fileName(),
                searchFileRequest.extension(),
                searchFileRequest.type(),
                searchFileRequest.startDate(),
                searchFileRequest.endDate(),
                pageNumber,
                pageSize,
                sort
        );

        PageResultOutput<FileMetadataOutput> execute = searchFilesUseCase.execute(searchFilesFilter);

        PageResponse<FileMetadata> pageResponse = new PageResponse<>(
                execute.content().stream().map(FileMetadata::from).toList(),
                execute.page(),
                execute.size(),
                execute.totalElements(),
                execute.totalPages());

        log.info("Files searched successfully");
        return ResponseEntity.ok(pageResponse);
    }
}