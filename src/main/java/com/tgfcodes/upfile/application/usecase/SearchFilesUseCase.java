package com.tgfcodes.upfile.application.usecase;

import com.tgfcodes.upfile.application.annotations.AppTransactional;
import com.tgfcodes.upfile.application.input.SearchFilesFilter;
import com.tgfcodes.upfile.application.input.SortOption;
import com.tgfcodes.upfile.application.query.FileMetadataOutput;
import com.tgfcodes.upfile.application.query.PageResultOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.storedfile.PageResult;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFileFilter;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;

@AppService
public class SearchFilesUseCase {

    private final StoredFiles storedFiles;

    public SearchFilesUseCase(final StoredFiles storedFiles) {
        this.storedFiles = storedFiles;
    }

    @AppTransactional(readOnly = true)
    public PageResultOutput<FileMetadataOutput> execute(SearchFilesFilter searchFilesFilter) {
        Checks.requireNonNull(searchFilesFilter, () -> new DomainValidationException("Search files query cannot be null"));

        StoredFileFilter storedFileFilter = buildStoredFileFilter(searchFilesFilter);
        PageResult<StoredFile> pageResult = storedFiles.search(storedFileFilter);

        return new PageResultOutput<>(
                pageResult.content().stream().map(FileMetadataOutput::from).toList(),
                pageResult.page(),
                pageResult.size(),
                pageResult.totalElements(),
                pageResult.totalPages()
        );
    }

    private StoredFileFilter buildStoredFileFilter(SearchFilesFilter searchFilesFilter) {

        StoredFileFilter.Page page = new StoredFileFilter.Page(searchFilesFilter.pageNumber(), searchFilesFilter.pageSize());
        SortOption sortOption = SortOption.from(searchFilesFilter.sort());

        StoredFileFilter.Sort sort = new StoredFileFilter.Sort(
                sortOption.getField(),
                sortOption.getDirection()
        );

        return new StoredFileFilter(
                searchFilesFilter.fileName(),
                searchFilesFilter.extension(),
                searchFilesFilter.type(),
                searchFilesFilter.startDate(),
                searchFilesFilter.endDate(),
                page,
                sort
        );
    }
}
