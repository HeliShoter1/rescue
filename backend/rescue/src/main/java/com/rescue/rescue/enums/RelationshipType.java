package com.rescue.rescue.enums;

public enum RelationshipType {
    SPOUSE,
    PARENT,
    CHILD,
    SIBLING,
    OTHER;

    public static RelationshipType fromString(String val) {
        if (val == null) return OTHER;
        String normalized = val.trim().toUpperCase();
        switch (normalized) {
            case "SPOUSE":
            case "VỢ CHỒNG":
            case "VO CHONG":
            case "VỢ":
            case "CHỒNG":
            case "VO":
            case "CHONG":
                return SPOUSE;
            case "PARENT":
            case "CHA MẸ":
            case "CHA ME":
            case "CHA":
            case "MẸ":
            case "BỐ":
            case "ME":
            case "BO":
                return PARENT;
            case "CHILD":
            case "CON":
                return CHILD;
            case "SIBLING":
            case "ANH CHỊ EM":
            case "ANH CHI EM":
            case "ANH":
            case "CHỊ":
            case "EM":
            case "CHI":
                return SIBLING;
            case "OTHER":
            case "KHÁC":
            case "KHAC":
            default:
                return OTHER;
        }
    }
}
