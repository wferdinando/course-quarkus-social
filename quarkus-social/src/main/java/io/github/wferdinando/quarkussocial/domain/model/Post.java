package io.github.wferdinando.quarkussocial.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_text")
    private String text;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void setDateTimePost(){
        setDateTime(LocalDateTime.now());
    }
}
