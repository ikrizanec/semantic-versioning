package com.infobip.pmf.course.semanticversioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/versions")
public class SemanticVersioningController {
    @Autowired
    public SemanticVersioningService semanticVersioningService;

    @GetMapping("/max")
    public String getMax(@RequestParam("v1") String version1, @RequestParam("v2") String version2) {
        if(!semanticVersioningService.isValidVersion(version1))
            return "Verzion \"%s\" does not adhere to the SemVer 2.0.0 specification".formatted(version1);
        if(!semanticVersioningService.isValidVersion(version2))
            return "Verzion \"%s\" does not adhere to the SemVer 2.0.0 specification".formatted(version2);
        return semanticVersioningService.returnLatestVersion(version1,version2);
    }

    @GetMapping("/next")
    public String getNext(@RequestParam("v") String version, @RequestParam("type") String type) {
        if(!semanticVersioningService.isValidVersion(version))
            return "Verzion" + version + "does not adhere to the SemVer 2.0.0 specification.";
        if(!semanticVersioningService.isValidType(type))
            return "Type \"%s\" is not supported type value.".formatted(type);
        return semanticVersioningService.returnNextVersion(version, type);
    }
}
