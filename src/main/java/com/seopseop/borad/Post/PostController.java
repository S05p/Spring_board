package com.seopseop.borad.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping("/list/{page}")
    public String list (@PathVariable Long page,
                        Model model) {

        int a = page.intValue();

        Page<Post> results = postRepository.findAll(PageRequest.of(a - 1, 5,Sort.by("id").descending()));
        int totalPages = results.getTotalPages();
        int currentPage = a;
        int startPage = Math.max(1, currentPage - 5);
        int endPage = Math.min(totalPages, currentPage + 4);
        if (endPage - startPage < 9) {
            if (startPage == 1) {
                endPage = Math.min(startPage + 9, totalPages);
            } else if (endPage == totalPages) {
                startPage = Math.max(1, endPage - 9);
            }
        }
        model.addAttribute("results", results.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("hasNext", results.hasNext());
        model.addAttribute("hasPrevious", results.hasPrevious());
        return "list.html";
    }

    @GetMapping("/create")
    public String GetCreate () {
        return "create.html";
    }

    @PostMapping("/create")
    public String PostCreate(@RequestParam String title,
                             @RequestParam String content) {
        postService.postsave(title,content);
        return "redirect:/list/1";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id,
                         Model model) {
        Optional<Post> result = postRepository.findById(id);
        if (result.isEmpty()) {
            return "redirect:/list/1";
        }
        model.addAttribute("post",result.get());
        return "detail.html";
    }

    @GetMapping("/update/{id}")
    public String GetUpdate(@PathVariable Long id,
                            Model model) {
        Optional<Post> result = postRepository.findById(id);
        if (result.isEmpty()) {
            return "redirect:/list/1";
        }
        model.addAttribute("post",result.get());
        return "update.html";
    }

    @PostMapping("/update/{id}")
    public String PostUpdate (@RequestParam String title,
                              @RequestParam String content,
                              @PathVariable Long id) {

        Optional<Post> result = postRepository.findById(id);
        Post post = result.get();
        post.setTitle(title);
        post.setContent(content);
        post.setId(id);
        postRepository.save(post);
        return "redirect:/list/1";
    }

    @GetMapping("/delete/{id}")
    public String delete (@PathVariable Long id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = result.get();
        postRepository.delete(post);
        return "redirect:/list/1";
    }
}
