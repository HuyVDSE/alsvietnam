package com.alsvietnam.converter;

import com.alsvietnam.repository.*;
import com.alsvietnam.service.FileService;
import com.alsvietnam.service.FileStorageService;
import com.alsvietnam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class BaseConverter {

    // service

    @Autowired
    protected UserService userService;

    @Autowired
    protected FileService fileService;

    // repository

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected ArticleContentRepository articleContentRepository;

    @Autowired
    protected ArticleMediaRepository articleMediaRepository;

    @Autowired
    protected ArticleFileRepository articleFileRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserTaskRepository userTaskRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected HonoredUserRepository honoredUserRepository;

    // other

    @Autowired
    protected FileStorageService fileStorageService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected Tika tika;
}
