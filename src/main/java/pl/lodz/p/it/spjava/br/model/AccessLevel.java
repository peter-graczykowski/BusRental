/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.model;

public enum AccessLevel {
    
    ACCOUNT(AccessLevelKeys.ACCOUNT_KEY), 
    NEWACCOUNT(AccessLevelKeys.NEWACCOUNT_KEY),
    ADMIN(AccessLevelKeys.ADMIN_KEY),
    PLANIST(AccessLevelKeys.PLANIST_KEY), 
    CLIENT(AccessLevelKeys.CLIENT_KEY); 
    
    private AccessLevel(final String key) {
        this.accessLevelKey = key;
    }
    private String accessLevelKey;
    private String accessLevelI18NValue;

    public String getAccessLevelKey() {
        return accessLevelKey;
    }

    public String getAccessLevelI18NValue() {
        return accessLevelI18NValue;
    }

    public void setAccessLevelI18NValue(String accessLevelI18NValue) {
        this.accessLevelI18NValue = accessLevelI18NValue;
    }

    public static class AccessLevelKeys {
        public static final String ACCOUNT_KEY = "access.level.account";
        public static final String NEWACCOUNT_KEY = "access.level.newaccount";
        public static final String ADMIN_KEY = "access.level.admin";
        public static final String PLANIST_KEY = "access.level.planist";
        public static final String CLIENT_KEY = "access.level.client";
    }       
}
    
