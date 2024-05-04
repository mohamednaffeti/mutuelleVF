package com.cpg.mutuelle.controllers;

import com.cpg.mutuelle.entities.Actualite;
import com.cpg.mutuelle.entities.Image;
import com.cpg.mutuelle.entities.dto.ImageUploadResponse;
import com.cpg.mutuelle.repositories.ActualiteRepository;
import com.cpg.mutuelle.repositories.ImageRepository;
import com.cpg.mutuelle.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ActualiteRepository actualiteRepository;

    @PostMapping(value = "/upload/imageActualite/{idActualite}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImageActualite(@RequestParam("image") MultipartFile file,
                                                                  @PathVariable Long idActualite)
            throws IOException {


        Optional<Actualite> optionalActualite = actualiteRepository.findById(idActualite);
        if (optionalActualite.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ImageUploadResponse("Actualite not found"));
        }
        imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes()))
                .actualite(optionalActualite.get())
                .build());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));
    }

}
