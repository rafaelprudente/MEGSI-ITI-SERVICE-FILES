package pt.iti.umdrive.controller;

import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pt.iti.umdrive.model.FileModel;
import pt.iti.umdrive.persistence.entities.FileEntity;
import pt.iti.umdrive.service.FilesService;
import pt.megsi.fwk.exceptions.BusinessException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;

    @GetMapping
    public ResponseEntity<List<FileModel>> getFiles() {
        return ResponseEntity.ok(filesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadById(@PathVariable(name = "id") UUID id) throws MalformedURLException, FileNotFoundException {
        Pair<FileEntity, Resource> response = filesService.downloadResource(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new File(response.getValue0().getOriginalName()).getName() + "\"")
                .contentType(MediaType.parseMediaType(response.getValue0().getMimeType()))
                .contentLength(response.getValue0().getSize())
                .body(response.getValue1());
    }

    @PostMapping
    public ResponseEntity<FileModel> save(@RequestParam(name = "path", defaultValue = "/") String path, @RequestParam("file") MultipartFile file) throws BusinessException {
        FileModel fm = filesService.save(Paths.get(path), file);

        return ResponseEntity.ok(fm);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID id) throws BusinessException {
        filesService.deleteById(id);

        return ResponseEntity.accepted().body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam(name = "path", defaultValue = "/") String path, @RequestParam("file") String file) throws BusinessException {
        filesService.delete(Paths.get(path), file);

        return ResponseEntity.accepted().body(null);
    }
}
