package com.digimenu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "file_storage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileStorage extends CompanyAware {
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

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public FileStorage(String fileName, String fileType, long size, String url, Company company, User user) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.url = url;
        this.company = company;
        this.user = user;
    }
}
