package ru.slevyns.testtask.service.counter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.slevyns.testtask.domain.DirRequest;
import ru.slevyns.testtask.domain.Word;
import ru.slevyns.testtask.mapper.DirMapper;
import ru.slevyns.testtask.service.word_counter.file.FileService;
import ru.slevyns.testtask.service.word_counter.filter.FilterService;
import ru.slevyns.testtask.service.word_counter.impl.WordCountServiceImpl;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static ru.slevyns.testtask.meta.TestMeta.CHANGED_MIN_TOP;
import static ru.slevyns.testtask.meta.TestMeta.COUNTER_TEST_DIR_PATH;
import static ru.slevyns.testtask.meta.TestMeta.MIN_LENGTH;
import static ru.slevyns.testtask.meta.TestMeta.TOP_NUM;

@ExtendWith(MockitoExtension.class)
class WordCountServiceImplTest {
    @Mock
    private FileService fileService;
    @Mock
    private FilterService filterService;
    @Mock
    private DirMapper mapper;

    @InjectMocks
    private WordCountServiceImpl wordCountService;

    @Test
    void countWords_defaultMinWordLengthDefaultTopN_returnFilteredResult() {
        var request = new DirRequest(COUNTER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);

        var path = Path.of(request.dirPath());
        doReturn(path)
                .when(mapper)
                .toDirectoryPath(request);

        var allWordsSet = fillAllWordsSet();
        doReturn(allWordsSet)
                .when(fileService)
                .processFiles(path);

        doReturn(allWordsSet)
                .when(filterService)
                .filter(allWordsSet);

        var result = wordCountService.countWords(request);
        assertEquals(9, result.size());

        verify(mapper).toDirectoryPath(request);
        verify(fileService).processFiles(path);
        verify(filterService).filter(allWordsSet);
    }

    @Test
    void countWords_changedMinWordLengthDefaultTopN_returnFilteredResult() {
        var request = new DirRequest(COUNTER_TEST_DIR_PATH, CHANGED_MIN_TOP, CHANGED_MIN_TOP);

        var path = Path.of(request.dirPath());
        doReturn(path)
                .when(mapper)
                .toDirectoryPath(request);

        var allWordsSet = fillAllWordsSet();
        doReturn(allWordsSet)
                .when(fileService)
                .processFiles(path);

        var filtered = fillTop3Filtered();
        doReturn(filtered)
                .when(filterService)
                .filter(allWordsSet);

        var result = wordCountService.countWords(request);
        assertEquals(3, result.size());

        verify(mapper).toDirectoryPath(request);
        verify(fileService).processFiles(path);
        verify(fileService).changeMinWordLength(CHANGED_MIN_TOP);
        verify(filterService).changeTopWordsNum(CHANGED_MIN_TOP);
        verify(filterService).filter(allWordsSet);
    }

    private Set<Word> fillAllWordsSet() {
        var word1 = new Word("four", 3);
        var word2 = new Word("five", 3);
        var word3 = new Word("nine", 3);
        var word4 = new Word("twelve", 2);
        var word5 = new Word("three", 9);
        var word6 = new Word("seven", 7);
        var word7 = new Word("abracadabra", 3);
        var word8 = new Word("eight", 5);
        var word9 = new Word("eleven", 2);

        return Set.of(word1, word2, word3, word4, word5, word6, word7, word8, word9);
    }

    private Set<Word> fillTop3Filtered() {
        var word1 = new Word("three", 9);
        var word2 = new Word("seven", 7);
        var word3 = new Word("eight", 5);
        return Set.of(word1, word2, word3);
    }
}