package com.example.devrelhelper.model;

import java.util.List;

public record TalkWrapped(List<String> tweets, String blogTitle, String blogLede, List<BlogSection> blogSections) {
}
