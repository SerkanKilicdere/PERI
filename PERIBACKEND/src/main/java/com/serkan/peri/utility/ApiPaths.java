package com.serkan.peri.utility;

public class ApiPaths {


    private static final String VERSION = "/v1";
    private static final String DEV = "/dev";
    private static final String BASE_URL = DEV + VERSION;

    //--------------------------------------------------------------------------

    public static final String HUMANRESOURCE = BASE_URL + "/humanresource";
    public static final String USER = BASE_URL + "/user";
    public static final String PASSWORD = BASE_URL + "/password";
    public static final String SIGNIN = BASE_URL + "/signin";
    public static final String CONSUMER = BASE_URL + "/consumer";
    public static final String SYSTEMADMINISTRATOR = BASE_URL + "/systemadministrator";
    public static final String COMPANYADMINISTRATOR = BASE_URL + "/companyadministrator";
    public static final String CREATEPASSWORD =  "/createpassword";

    public static final String LOGIN =   "/login";

    public static final String RECORDUSER =  "/recorduser";
    public static final String PROFILE = "/profile";
    public static final String VERIFY =  "/verifyemail";
    public static final String REGISTER =  "/register";

    public static final String BUY= "/buyapplication";
    public static final String CONTACT = "/contact";
    public static final String QUOTE = "/quote";
    public static final String DELETECOMPANY = "/deletecompany/{companyId}";

    public static final String VERIFYCOMPANYEMAIL = "/verifycompanyemail";
    public static final String ASSIGNSYSTEMADMIN = "/assign-system-admin";
    public static final String ASSIGNCOMPANYADMIN = "/assign-company-admin";
    public static final String CONTACT_MESSAGES = "/contact-messages";
    public static final String MARK_READ = "/contact-messages/{id}/read";
    public static final String QUOTE_REQUESTS = "/quote-requests";
    public static final String QUOTE_MARK_READ = "/quote-requests/{id}/read";

}
