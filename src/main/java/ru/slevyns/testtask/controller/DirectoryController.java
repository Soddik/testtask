package ru.slevyns.testtask.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.slevyns.testtask.domain.DirRequest;
import ru.slevyns.testtask.service.word_counter.WordCountService;
import ru.slevyns.testtask.service.validation.ValidationService;

@Controller
@RequestMapping("directory/find")
public class DirectoryController {
    private static final Logger log = LoggerFactory.getLogger(DirectoryController.class);
    private final WordCountService wordCountService;
    private final ValidationService<DirRequest> validationService;

    public DirectoryController(WordCountService wordCountService, ValidationService<DirRequest> validationService) {
        this.wordCountService = wordCountService;
        this.validationService = validationService;
    }

    @GetMapping("words")
    public String findWords() {
        return "directory/find/words";
    }

    @PostMapping("/words")
    public String findWords(Model model, @ModelAttribute("request") DirRequest request) {
        model.addAttribute("dirRequest", request);

        var errors = validationService.validate(request);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "/directory/find/validation_error";
        }

        log.info("Retrieving words from {}", request.dirPath());
        var words = wordCountService.countWords(request);
        model.addAttribute("result", words);
        log.info("Redirecting to {}", "directory/find/result");
        return "/directory/find/result";
    }
}

