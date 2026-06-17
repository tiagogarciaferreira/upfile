package com.tgfcodes.upfile.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_stored_files")
@EntityListeners(AuditingEntityListener.class)
public class StoredFileEntity {

    @Id
    private UUID id;

    private String bucket;

    @Column(name = "file_name")
    private String fileName;

    private String key;

    @Column(name = "e_tag")
    private String eTag;

    private String hash;

    private String extension;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content_disposition")
    private String contentDisposition;

    private Long size;

    private String type;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by_user_id")
    private UUID createdByUserId;
}
