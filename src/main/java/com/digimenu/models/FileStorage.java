package com.digimenu.models;

import com.digimenu.interfaces.CompanyAware;
import com.digimenu.listeners.CompanyEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

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

    private String url;

    public String getUrl() {
        return "http://192.168.0.82:8080/file/" + id;
    }
}
