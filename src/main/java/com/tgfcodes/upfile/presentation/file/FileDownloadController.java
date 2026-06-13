package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.output.FileDownloadOutput;
import com.tgfcodes.upfile.application.usecase.DownloadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/files", version = "1.0")
public class FileDownloadController implements DownloadsApi {

    private final DownloadFileUseCase downloadFileUseCase;

    @Override
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {

        FileDownloadOutput fileDownload = downloadFileUseCase.execute(id);
        InputStreamResource inputStreamResource = new InputStreamResource(fileDownload.stream());

        return ResponseEntity.ok()
                .contentLength(fileDownload.size())
                .contentType(MediaType.parseMediaType(fileDownload.contentType()))
                .header(CONTENT_DISPOSITION, "%s; filename=\"%s\"".formatted(fileDownload.contentDisposition(), fileDownload.fileName()))
                .body(inputStreamResource);
    }
}
