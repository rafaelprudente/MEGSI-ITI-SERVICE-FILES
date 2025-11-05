package pt.iti.umdrive.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.iti.umdrive.exceptions.BusinessException;
import pt.iti.umdrive.model.FileModel;
import pt.iti.umdrive.persistence.entities.AuthUserEntity;
import pt.iti.umdrive.persistence.repositories.FileRepository;
import pt.iti.umdrive.persistence.repositories.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Value("${app.root.folder}")
    Path appRootFolder;

    public List<FileModel> findAll() {
        AuthUserEntity ue = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        return fileRepository.findAll().stream().map(f -> FileModel.builder().id(f.getId()).build()).toList();
    }

    public FileModel save(Path path, MultipartFile file) throws BusinessException {
        if (file.isEmpty()) {
            throw new BusinessException("File " + file.getOriginalFilename() + " is empty!");
        }

        log.debug("Start - FileModel saveFile(MultipartFile file)");

        FileModel result;

        try {
            String destinationFileName = Objects.requireNonNull(FilenameUtils.getName(file.getOriginalFilename())).replaceAll("." + FilenameUtils.getExtension(file.getOriginalFilename()), "");
            AuthUserEntity ue = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            Path uploadPath = Paths.get(appRootFolder.toString(), ue.getId().toString(), path.toString());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            List<String> results = SearchFileByWildcard.searchWithWc(uploadPath, destinationFileName + "_V\\d+." + FilenameUtils.getExtension(file.getOriginalFilename()));

            destinationFileName = destinationFileName + "_V" + results.size() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

            Path filePath = uploadPath.resolve(destinationFileName);

            Files.copy(file.getInputStream(), filePath);

            result = FileModel.builder().name(file.getOriginalFilename()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            log.debug("End - FileModel saveFile(MultipartFile file)");
        }

        return result;
    }

    public void delete(Path path, String file) {
        String destinationFileName = Objects.requireNonNull(FilenameUtils.getName(file)).replaceAll("." + FilenameUtils.getExtension(file), "");
        List<String> results = SearchFileByWildcard.searchWithWc(path, destinationFileName + "_V\\d+." + FilenameUtils.getExtension(file));
    }

    public void deleteById(UUID id) {

    }
}
