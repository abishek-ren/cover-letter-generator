package tech.abishek.ai1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.abishek.ai1.model.Resume;

@Repository
@Transactional(readOnly = true)
public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
