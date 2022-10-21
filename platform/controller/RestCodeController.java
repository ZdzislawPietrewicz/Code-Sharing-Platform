package platform.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.model.CodeEntity;
import platform.service.CodeSharingService;
import java.util.*;


@RestController
@RequestMapping(value = "/api/code")
public class RestCodeController {
    @Autowired
    CodeSharingService codeSharingService;

    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CodeEntity> getCodeFromApi(@PathVariable UUID uuid) {
        CodeEntity codeEntity = codeSharingService.getCodeEntityByUuid(uuid).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT EXIST"));
        return new ResponseEntity<>(codeEntity, HttpStatus.OK);
    }


    @PostMapping(value = "/new")
    public Map<String, String> postNewCodeEntity(@RequestBody CodeEntity codeEntity) {
        codeSharingService.postNewCodeEntity(codeEntity);
        return Collections.singletonMap("id", String.valueOf(codeEntity.getUuid()));
    }

    @GetMapping(value = "/latest")
    public List<CodeEntity> getLatestCodes() {
        return codeSharingService.getLatestTenCodes();
    }

}
