package com.serkan.peri.entity.utility.userutilities;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum UserCategories {
    //APPLİKASYON SAHİBİ ŞİRKET YETKİLİSİ
    APPLICATION_OWNER("APP_OWNER", "Application Owner Company Representative", 10),

    // ÜST YÖNETİM & STRATEJİ
    EXECUTIVE("EXEC", "C-Level and VPs", 9),
    BOARD_MEMBER("BOARD", "Board of Directors", 9),

    // YÖNETİM
    STRATEGIC_PLANNING("STRAT", "Business Strategy and Development", 8),
    REGIONAL_MANAGER("REG", "Regional and Country Heads", 8),
    DEPARTMENT_HEAD("DEPT", "General Department Managers", 7),

    // TEKNOLOJİ & DİJİTAL
    TECH_IT("TECH", "IT, Software and Infrastructure", 6),
    DATA_ANALYTICS("DATA", "Data Science and Business Intelligence", 6),
    CYBER_SECURITY("SEC", "Information Security and Risk", 7),

    // OPERASYON & ÜRETİM
    SUPPLY_CHAIN("SUPPLY", "Logistics and Procurement", 5),
    MANUFACTURING("MFG", "Plant Operations and Production", 4),
    QUALITY_CONTROL("QC", "Quality Assurance and Standards", 5),

    // TİCARİ & PAZARLAMA
    SALES_RETAIL("SALES", "B2B and Retail Sales Force", 4),
    MARKETING_BRAND("MKTG", "Brand Management and Advertising", 5),
    PUBLIC_RELATIONS("PR", "Communications and Media", 6),

    // KURUMSAL DESTEK
    HUMAN_RESOURCES("HR", "Talent Acquisition and Employee Relations", 6),
    FINANCE_TREASURY("FIN", "Accounting, Audit and Treasury", 7),
    LEGAL_COMPLIANCE("LEGAL", "Legal Affairs and Compliance", 8),
    SUSTAINABILITY("SUST", "ESG and Environmental Sustainability", 6),

    // AR-GE & İNOVASYON
    RESEARCH_DEV("RD", "Product Development and Lab Research", 7),

    // SAHA VE GİRİŞ SEVİYESİ OPERASYONLAR
    GENERAL_LABOR("LABOR", "General Manual Labor", 1),
    WAREHOUSE_STAFF("WHSE", "Warehouse and Storage Workers", 2),
    DISTRIBUTION_ENTRY("DIST", "Delivery and Loading Personnel", 2),
    FACILITY_MAINTENANCE("MAINT", "General Cleaning and Maintenance", 1),
    SEASONAL_WORKER("SEASON", "Temporary Seasonal Support", 1),

    // DIŞ KAYNAK & SAHA
    CONTRACTOR("EXT", "Third-party Consultants", 2),
    FIELD_OPERATIVE("FIELD", "Frontline Field Workers", 1),
    // DEMO USER
    DEMO_USER("DEMO", "Demo Users", 1);

    private final String code;
    private final String description;
    private final int accessLevel; // 1-10 arası yetki seviyesi

    UserCategories(String code, String description, int accessLevel) {
        this.code = code;
        this.description = description;
        this.accessLevel = accessLevel;
    }

    // Getters
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public int getAccessLevel() { return accessLevel; }





}
