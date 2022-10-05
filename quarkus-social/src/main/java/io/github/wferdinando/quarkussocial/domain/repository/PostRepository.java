package io.github.wferdinando.quarkussocial.domain.repository;

import io.github.wferdinando.quarkussocial.domain.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository  implements PanacheRepository<Post> {


    public void deleteByPostAndUser(Long postId, Long userId){
        var params = Parameters.with("userId", userId)
                .and("postId", postId).map();

        delete("id =:postId and user.id =:userId", params);
    }
}
