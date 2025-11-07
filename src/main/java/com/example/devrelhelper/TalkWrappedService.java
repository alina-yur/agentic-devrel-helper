package com.example.devrelhelper;

import com.example.devrelhelper.model.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TalkWrappedService {
    private final com.embabel.agent.core.Ai ai;

    public TalkWrappedService(com.embabel.agent.core.Ai ai) {
        this.ai = ai;
    }

    public TalkWrapped generate(Talk in) {
        var prompt = template()
                .replace("${title}", in.title())
                .replace("${conference}", in.conference())
                .replace("${shortDesc}", in.shortDesc())
                .replace("${demos}", String.join(", ", in.demos() == null ? List.of() : in.demos()));
        return ai.withAutoLlm().createObject(prompt, TalkWrapped.class);
    }

    public String toText(Talk in, TalkWrapped t) {
        var b = new StringBuilder();
        b.append("TITLE: ").append(in.title()).append("\n");
        b.append("CONFERENCE: ").append(in.conference()).append("\n");
        b.append("DESCRIPTION: ").append(in.shortDesc()).append("\n\n");

        b.append("TWEETS:\n");
        t.tweets().forEach(s -> b.append("- ").append(s).append("\n"));
        b.append("\n");

        b.append("BLOG TITLE:\n").append(t.blogTitle()).append("\n\n");
        b.append("BLOG LEDE:\n").append(t.blogLede()).append("\n\n");

        b.append("BLOG SECTIONS:\n");
        for (var s : t.blogSections()) {
            b.append(s.heading()).append("\n");
            for (var bl : s.bullets())
                b.append("- ").append(bl).append("\n");
            b.append("\n");
        }

        if (in.demos() != null && !in.demos().isEmpty()) {
            b.append("DEMOS:\n");
            in.demos().forEach(d -> b.append(d.trim()).append("\n"));
        }
        return b.toString();
    }

    private String template() {
        try {
            var r = new ClassPathResource("prompts/talk_wrapped.txt");
            return new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
