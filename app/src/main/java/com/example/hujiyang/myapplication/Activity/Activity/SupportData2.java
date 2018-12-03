package com.example.hujiyang.myapplication.Activity.Activity;

public class SupportData2 {
    public String getDatabaseName() {
        return "cs650";
    }

    public String getApiKey() {
        return "1t3KBYAWFao-Z-gqUmnhJNuVCLcjbA5Z";
    }

    public String getBaseUrl() {
        return "https://api.mlab.com/api/1/databases/" + getDatabaseName() + "/collections/";
    }

    public String apiKeyUrl() {
        return "?apiKey=" + getApiKey();
    }

    public String collectionName() {
        return "currentTime";
    }


    public String buildContactsFetchURL() {
        return getBaseUrl() + collectionName() + apiKeyUrl();
    }

    public String buildContactsSaveURL() {
        return getBaseUrl() + collectionName() + apiKeyUrl();
    }

    public String createContact(MyContact2 contact2) {
        return String
                .format("{\"time\": \"%s\"}",
                        contact2.getTime());
    }
}
