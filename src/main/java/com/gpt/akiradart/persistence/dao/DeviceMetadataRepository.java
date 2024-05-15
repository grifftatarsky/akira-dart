package com.gpt.akiradart.persistence.dao;

import com.gpt.akiradart.persistence.model.DeviceMetadata;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {

  List<DeviceMetadata> findByUserId(Long userId);
}