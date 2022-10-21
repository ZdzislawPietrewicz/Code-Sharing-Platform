package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import platform.model.CodeEntity;
import platform.service.CodeSharingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping(value = "/code")
public class CodeController {

    @Autowired
    CodeSharingService codeSharingService;

    @GetMapping(value = "/{uuid}")
    public ModelAndView getSecretCode(@PathVariable UUID uuid) {
        var model = new ModelAndView();
        Optional<CodeEntity> codeEntity = codeSharingService.getCodeEntityByUuid(uuid);
        codeEntity.ifPresentOrElse((code) -> {
            model.addObject("code", code);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND");
        });
        model.setViewName("/code");
        return model;
    }

    @GetMapping(value = "/new")
    public String showNewFormToAddNewCode() {
        return "create";
    }

    @GetMapping(value = "/latest")
    public String getLatestCodes(Model model) {
        model.addAttribute("latestCodes", codeSharingService.getLatestTenCodes());
        return "latest";
    }
}
