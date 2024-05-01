package ru.slevyns.testtask.service.counter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.domain.DirRequest;
import ru.slevyns.testtask.domain.Word;
import ru.slevyns.testtask.mapper.DirMapper;
import ru.slevyns.testtask.service.file.FileService;
import ru.slevyns.testtask.service.counter.WordCountService;
import ru.slevyns.testtask.service.filter.FilterService;

import java.util.Set;

import static ru.slevyns.testtask.service.filter.FilterService.*;

@Service
public class WordCountServiceImpl implements WordCountService {
    private static final Logger log = LoggerFactory.getLogger(WordCountServiceImpl.class);
    private final FileService fileService;
    private final FilterService filterService;
    private final DirMapper mapper;

    public WordCountServiceImpl(FileService fileService, FilterService filterService, DirMapper mapper) {
        this.fileService = fileService;
        this.filterService = filterService;
        this.mapper = mapper;
    }

    @Override
    public Set<Word> countWords(DirRequest request) {
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

        return filtered;
    }
}
