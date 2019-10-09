package be.db;

import be.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("select s from Song s where skipped = 'false' and songId = ?1")
    Song findSongBySongId(String songId);

    @Query("select s from Song s where skipped = 'false' and priority = ?1")
    Song findSongByPriority(int priority);

    @Query("select s from Song s where skipped = 'false' order by priority")
    List<Song> findAll();

    @Modifying
    @Query("update Song set skipped = 'true' where id = ?1")
    @Transactional
    void deleteById(Long id);
}
