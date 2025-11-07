package com.example.devrelhelper;

import com.example.model.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.*;
import java.time.*;
import java.util.*;

@SpringBootApplication
public class DevrelHelperApplication implements CommandLineRunner {
	private final TalkWrappedService svc;
	private final com.embabel.agent.core.Ai ai;

	public DevrelHelperApplication(com.embabel.agent.core.Ai ai) {
		this.ai = ai;
		this.svc = new TalkWrappedService(ai);
	}

	public static void main(String[] args) {
		SpringApplication.run(DevrelHelperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var in = new Talk(
				env("TITLE", "GraalVM in practice"),
				env("CONFERENCE", "Devoxx Morocco 2025"),
				env("DESC", "A practical deep dive in GraalVM native Image."),
				List.of(env("DEMOS", "https://github.com/alina-yur/graalvm-in-practice").split(",")));

		var out = svc.generate(in);
		var text = svc.toText(in, out);

		Files.writeString(Path.of("talk-wrapped.txt"), text);
		System.out.println("✅ Generated talk-wrapped.txt");
	}

	private String env(String key, String defaultValue) {
		String value = System.getenv(key);
		return value != null ? value : defaultValue;
	}
}