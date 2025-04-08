package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.cabinetmedicalproject.model.Archive;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    // Vous pouvez ajouter des méthodes de requêtes personnalisées ici si nécessaire
}
