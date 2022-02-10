package com.sn.org.SpBoot2.repo;

import com.sn.org.SpBoot2.model.Post;
import com.sn.org.SpBoot2.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByTitle(String title);
    Post findByTitle(String title);
    List<Post> findAllByAuthorId(long authorId);


}
