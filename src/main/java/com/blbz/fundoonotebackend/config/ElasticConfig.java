package com.blbz.fundoonotebackend.config;

import com.blbz.fundoonotebackend.entiry.NoteInfoEl;
import com.blbz.fundoonotebackend.repository.elasticsearch.NoteElRepo;
import com.blbz.fundoonotebackend.repository.jpa.NoteRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.blbz.fundoonotebackend")
public class ElasticConfig {
   /* private final NoteElRepo noteElRepo;
    private final ColorRepo colorRepo;
    private final LabelRepo labelRepo;
    private final NoteRepo noteRepo;
    private final NoteStatusRepo noteStatusRepo;
    private final PicRepo picRepo;
    private final UserRepo userRepo;
    private final UserStatusRepo userStatusRepo;

    @Autowired
    public ElasticConfig(NoteElRepo noteElRepo,  ColorRepo colorRepo, LabelRepo labelRepo, NoteRepo noteRepo, NoteStatusRepo noteStatusRepo, PicRepo picRepo, UserRepo userRepo, UserStatusRepo userStatusRepo) {
        this.noteElRepo = noteElRepo;
        this.colorRepo = colorRepo;
        this.labelRepo = labelRepo;
        this.noteRepo = noteRepo;
        this.noteStatusRepo = noteStatusRepo;
        this.picRepo = picRepo;
        this.userRepo = userRepo;
        this.userStatusRepo = userStatusRepo;
    }
*/

    /* @Bean
     @Transactional
     public void loadAll() {
         noteElRepo.saveAll(noteRepo.findAll());
     }*/
    private final NoteElRepo noteElRepo;
    private final NoteRepo noteRepo;

    @Autowired
    public ElasticConfig(NoteElRepo noteElRepo, NoteRepo noteRepo) {
        this.noteElRepo = noteElRepo;
        this.noteRepo = noteRepo;
    }


    @Bean
    @Transactional
    public void loadAll() {
        List<NoteInfoEl> noteInfoEls = noteRepo.findAll().stream().map(noteInfo -> {
            NoteInfoEl noteInfoEl = new NoteInfoEl();
            BeanUtils.copyProperties(noteInfo, noteInfoEl);
            noteInfoEl.setCollaborator(noteInfo.getCollaborator());
            return noteInfoEl;
        }).collect(Collectors.toList());
        if (noteInfoEls.size() > 0) {
            noteElRepo.saveAll(noteInfoEls);
        }
    }
}
