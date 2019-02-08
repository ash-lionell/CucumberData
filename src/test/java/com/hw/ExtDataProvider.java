package com.hw;

import java.util.HashMap;

public interface ExtDataProvider {
    public HashMap<String, String[]> provideFor(String dataProviderName);
}
