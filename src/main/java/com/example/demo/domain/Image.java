package com.example.demo.domain;


import javax.persistence.*;

/**
 * Represents Image object. Used for persistence.
 *
 * @author lpetrenas
 */
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private java.lang.String name;
    private java.lang.String type;
    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] imageData;

    public Image() {
    }

    public Image(String name, String type, byte[] imageData) {
        this.name = name;
        this.type = type;
        this.imageData = imageData;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}