package com.example.hujiyang.myapplication.Activity.Activity;



public class SupportData {

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
        return "switch";
    }

    public String buildContactsSaveURL() {
        return getBaseUrl() + collectionName() + apiKeyUrl();
    }

    public String createContact(MyContact contact) {
        return String
                .format("{\"state\": \"%s\"}",
                        contact.getState());
    }

}
