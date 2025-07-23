package ca.sunlife.dynamo_db.controller;

import ca.sunlife.dynamo_db.entity.GeneratedAPI;
import ca.sunlife.dynamo_db.repository.GeneratedAPIRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generatedAPI")
public class GeneratedAPIController {

    private GeneratedAPIRepository generatedAPIRepository;
    public GeneratedAPIController(GeneratedAPIRepository generatedAPIRepository) {
        this.generatedAPIRepository = generatedAPIRepository;
    }

    @PostMapping
    public void createItem(@RequestBody GeneratedAPI generatedAPI) {
        generatedAPIRepository.save(generatedAPI);
    }

    @GetMapping("/{apiId}")
    public GeneratedAPI getItem(@PathVariable String apiId) {
        return generatedAPIRepository.getByApiId(apiId);
    }

    @GetMapping("/{apiId}/{version}")
    public GeneratedAPI getItemByVersion(@PathVariable String apiId, @PathVariable String version) {
        return generatedAPIRepository.getByApiIdAndVersion(apiId, version);
    }

    @GetMapping("/apiName/{apiName}")
    public List<GeneratedAPI> getByApiName(@PathVariable String apiName) {
        return generatedAPIRepository.getByApiName(apiName);
    }

    @GetMapping("/targetTopicName/{prefix}")
    public List<GeneratedAPI> getByTargetTopicNameLike(@PathVariable String prefix) {
        return generatedAPIRepository.getByTargetTopicNameLike(prefix);
    }

    @GetMapping("/source/{source}/{targetTopicName}")
    public List<GeneratedAPI> getBySource(@PathVariable String source, @PathVariable String targetTopicName) {
        return generatedAPIRepository.getBySourceAndTargetTopicNameLike(source, targetTopicName);
    }
}
