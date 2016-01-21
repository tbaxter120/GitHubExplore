package com.baxter.githubexplore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tbaxter on 1/20/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    public String name;
    public String description;

    @JsonProperty("html_url")
    public String url;
    public String id;

    @JsonProperty("forks_count")
    public int forksCount;

    @JsonProperty("stargazers_count")
    public int stargazersCount;
    public String language;
}
