package ru.astronarh.mosaic.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.astronarh.mosaic.model.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
