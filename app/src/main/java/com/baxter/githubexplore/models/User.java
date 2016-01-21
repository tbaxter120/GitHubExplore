package com.baxter.githubexplore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tbaxter on 1/20/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public String name;
    public String email;
    public String url;

    @JsonProperty("avatar_url")
    public String avatarUrl;
}
