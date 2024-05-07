package ru.slevyns.testtask.service.word_counter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.aspect.annotation.ExecutionTime;
import ru.slevyns.testtask.dto.dir.DirRequest;
import ru.slevyns.testtask.dto.dir.DirResponse;
import ru.slevyns.testtask.mapper.DirMapper;
import ru.slevyns.testtask.service.validation.ValidationService;
import ru.slevyns.testtask.service.word_counter.file.FileService;
import ru.slevyns.testtask.service.word_counter.WordCountService;
import ru.slevyns.testtask.service.word_counter.filter.FilterService;

import java.util.HashSet;

import static ru.slevyns.testtask.service.word_counter.filter.FilterService.*;

@Service
public class WordCountServiceImpl implements WordCountService {
    private static final Logger log = LoggerFactory.getLogger(WordCountServiceImpl.class);
    private final FileService fileService;
    private final FilterService filterService;
    private final ValidationService<DirRequest> validationService;
    private final DirMapper mapper;

    public WordCountServiceImpl(FileService fileService, FilterService filterService, ValidationService<DirRequest> validationService, DirMapper mapper) {
        this.fileService = fileService;
        this.filterService = filterService;
        this.validationService = validationService;
        this.mapper = mapper;
    }

    @ExecutionTime
    @Override
    public DirResponse countWords(DirRequest request) {
        var validationResults = validationService.validate(request);
        if (!validationResults.isEmpty()) {
            return mapper.toResponse(new HashSet<>(), validationResults);
        }

        log.info("Parse directory path");
        var path = mapper.toDirectoryPath(request);
        var wordMinLength = request.minLength();

        fileService.changeMinWordLength(wordMinLength);
        log.info("Word minimum length: {}", wordMinLength);

        log.info("Files processing start...");
        var wordCollection = fileService.processFiles(path);
        log.info("Files processing complete.");

        var topN = request.topN();
        if (topN != DEFAULT_TOP_NUM) {
            filterService.changeTopWordsNum(topN);
            log.info("Top words number changed on: {}", topN);
        }

        var filtered = filterService.filter(wordCollection);
        log.info("Words filtered.");

        return mapper.toResponse(filtered, new HashSet<>());
    }
}
