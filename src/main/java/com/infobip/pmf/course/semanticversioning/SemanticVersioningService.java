package com.infobip.pmf.course.semanticversioning;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.tomcat.util.http.parser.HttpParser.isNumeric;

@Service
public class SemanticVersioningService {

    public Boolean isValidVersion(String version){
        if(version == null){
            return false;
        }
        String versionPattern = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" +
                "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" +
                "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";

        Pattern pattern = Pattern.compile(versionPattern);
        Matcher matcher = pattern.matcher(version);
        return matcher.matches();
    }

    public boolean isValidType(String type){
        if(type == null){
            return false;
        }
        return type.equals("MAJOR") || type.equals("MINOR") || type.equals("PATCH");
    }


    public String returnLatestVersion(String version1, String version2) {
        String[] initialize1 = version1.split("\\+");
        String[] initialize2 = version2.split("\\+");
        String[] parts1 = initialize1[0].split("-");
        String[] parts2 = initialize2[0].split("-");
        String[] numPart1 = parts1[0].split("\\.");
        String[] numPart2 = parts2[0].split("\\.");

        int major1 = Integer.parseInt(numPart1[0]);
        int minor1 = Integer.parseInt(numPart1[1]);
        int patch1 = Integer.parseInt(numPart1[2]);

        int major2 = Integer.parseInt(numPart2[0]);
        int minor2 = Integer.parseInt(numPart2[1]);
        int patch2 = Integer.parseInt(numPart2[2]);

        switch (Integer.compare(major1, major2)) {
            case 1:
                return version1;
            case -1:
                return version2;
            default:
                switch (Integer.compare(minor1, minor2)) {
                    case 1:
                        return version1;
                    case -1:
                        return version2;
                    default:
                        switch (Integer.compare(patch1, patch2)) {
                            case 1:
                                return version1;
                            case -1:
                                return version2;
                            default:
                                if(checkPreRelaseVersions(parts1, parts2) == 1) {
                                    return version1;
                                } else {
                                    return version2;
                                }
                        }
                }
        }
    }

    private Integer checkPreRelaseVersions(String[] parts1, String[] parts2) {
        // major, minor, and patch are equal, a pre-release version has lower precedence than a normal version
        if(parts1.length == 1 && parts2.length > 1) return 1;
        if(parts2.length == 1 && parts1.length > 1) return 2;
        // comparing pre-release anotations
        for(int i = 1; i < parts1.length && i < parts2.length; i++){
            String[] little1 = parts1[i].split("\\.");
            String[] little2 = parts2[i].split("\\.");

            for(int j = 0; j < little1.length && j < little2.length; j++){
                if(compareTwoUnits(little1[j], little2[j]) != 0){
                    return compareTwoUnits(little1[j], little2[j]);
                }
            }
            if(little1.length > little2.length) return 1;
            if(little2.length > little1.length) return 2;
        }
        return 1;
    }

    public Boolean isNumber(String part){
        try{
            Integer.parseInt(part);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Integer compareTwoUnits(String unit1, String unit2){
        if(isNumber(unit1)){
            if(isNumber(unit2)){
                int broj1 = Integer.parseInt(unit1);
                int broj2 = Integer.parseInt(unit2);
                if (broj1 > broj2) return 1;
                if (broj2 > broj1) return 2;
            } else {
                return 2;
            }
        } else if (isNumber(unit2)){
            return 1;
        } else {
            if(unit1.compareTo(unit2) > 0) return 1;
            if(unit1.compareTo(unit2) < 0) return 2;
        }
        return 0;
    }

    public String returnNextVersion(String version, String type) {
        String[] initialize = version.split("\\+");
        String[] versionParts = initialize[0].split("-");
        String[] numParts = versionParts[0].split("\\.");

        int major = Integer.parseInt(numParts[0]);
        int minor = Integer.parseInt(numParts[1]);
        int patch = Integer.parseInt(numParts[2]);

        switch (type.toLowerCase()){
            case "major":
                major++;
                minor = 0;
                patch = 0;
                break;
            case "minor":
                minor++;
                patch = 0;
                break;
            case "patch":
                patch++;
                break;
            default:
                return "Type \"%s\" is not supported type value.".formatted(type);
        }
        return String.format("%d.%d.%d", major, minor, patch);
    }
}
