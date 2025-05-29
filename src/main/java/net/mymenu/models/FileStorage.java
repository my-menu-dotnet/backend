package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "file_storage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileStorage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "size")
    private long size;

    @JsonIgnore
    @Transient
    @Setter
    public static String fileUrl;

    @Transient
    private String url;

    public String getUrl() {
        return fileUrl + fileName;
    }
}
