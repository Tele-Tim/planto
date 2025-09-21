package planto_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import planto_project.model.Chapter;

import java.util.List;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
    List<Chapter> findAllBy();
    List<Chapter> findAllByChildrenEmpty();
    List<Chapter> findAllByParentIgnoreCase(String idParent);

//    @Query
//    List<ChapterProjection> findAllBy();
//    List<ChapterProjection> findAllByChildrenEmpty();
//    List<ChapterProjection> findAllByParentIgnoreCase(String idParent);

}
