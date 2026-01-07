package pt.iti.umdrive.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.iti.umdrive.domain.exception.BusinessException;
import pt.iti.umdrive.infrastructure.adapter.out.persistence.entity.FileEntity;
import pt.iti.umdrive.model.FileModel;
import pt.iti.umdrive.persistence.repositories.FileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesService {
    private final FileRepository fileRepository;

    @Value("${app.root_folder}")
    Path appRootFolder;
    @Value("${app.allowed_MimeTypes}")
    String[] appAllowedMimeTypes;

    public List<FileModel> findAll() {
        return fileRepository.findByUserId(UUID.fromString(Objects.requireNonNull(getClaim("id"))))
                .stream()
                .map(f -> FileModel.builder()
                        .id(f.getId())
                        .userId(f.getUserId())
                        .originalName(f.getOriginalName())
                        .storedName(f.getStoredName())
                        .mimeType(f.getMimeType())
                        .size(f.getSize())
                        .version(f.getVersion())
                        .createAt(f.getCreateAt())
                        .build())
                .toList();
    }

    public FileModel save(Path path, MultipartFile file) throws BusinessException {
        if (file.isEmpty()) {
            throw new BusinessException("File " + file.getOriginalFilename() + " is empty!");
        }

        log.debug("Start - FileModel saveFile(MultipartFile file)");

        FileModel result = null;

        try {
            String destinationFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

            Path uploadPath = Paths.get(appRootFolder.toString(), getClaim("id"), path.toString());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(destinationFileName);

            Files.copy(file.getInputStream(), filePath);

            String originalName = Paths.get(path.toString(), file.getOriginalFilename()).toString();
            long versionNumber = fileRepository.findByUserId(UUID.fromString(Objects.requireNonNull(getClaim("id")))).size();

            FileEntity fe = fileRepository.save(FileEntity.builder()
                    .userId(UUID.fromString(Objects.requireNonNull(getClaim("id"))))
                    .originalName(originalName)
                    .storedName(filePath.toString())
                    .mimeType(file.getContentType())
                    .size(filePath.toFile().length())
                    .version(versionNumber)
                    .createAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                    .build());

            result = FileModel.builder()
                    .id(fe.getId())
                    .userId(fe.getUserId())
                    .originalName(fe.getOriginalName())
                    .storedName(fe.getStoredName())
                    .mimeType(fe.getMimeType())
                    .size(fe.getSize())
                    .version(fe.getVersion())
                    .createAt(fe.getCreateAt()).build();
        } catch (Exception e) {
            throw new BusinessException(e.toString());
        } finally {
            log.debug("End - FileModel saveFile(MultipartFile file)");
        }

        return result;
    }

    public Pair<FileEntity, Resource> downloadResource(UUID id) throws FileNotFoundException, MalformedURLException {
        Pair<FileEntity, Resource> response = null;
        UrlResource resourceFile;

        Optional<FileEntity> ofe = fileRepository.findById(id);
        if (ofe.isPresent()) {
            resourceFile = new UrlResource(Paths.get(ofe.get().getStoredName()).toUri());
        } else {
            throw new FileNotFoundException("File not found!");
        }
        return new Pair<>(ofe.get(), resourceFile);
    }

    public void delete(Path path, String file) throws BusinessException {
        List<FileEntity> files = new ArrayList<>(); //fileRepository.findByUser_UsernameAndOriginalName(SecurityContextHolder.getContext().getAuthentication().getName(), path.resolve(file).toString());
        for (FileEntity fe : files) {
            deleteById(fe.getId());
        }

        //Optional<UserEntity> ue = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        //if (ue.isPresent()) {
        Path uploadPath = Paths.get("/");//Paths.get(appRootFolder.toString(), ue.get().getDetails().getId().toString(), path.toString());
        try (Stream<Path> paths = Files.list(uploadPath)) {
            if (paths.findAny().isEmpty()) {
                Files.deleteIfExists(uploadPath);
            }
        } catch (IOException e) {
            throw new BusinessException(e.toString());
        }
        //}
    }

    public void deleteById(UUID id) throws BusinessException {
        Optional<FileEntity> ofe = fileRepository.findById(id);
        if (ofe.isPresent()) {
            Path uploadPath = Paths.get(ofe.get().getStoredName());

            try {
                if (Files.deleteIfExists(uploadPath)) {
                    fileRepository.deleteById(id);
                }
            } catch (IOException e) {
                throw new BusinessException(e.toString());
            }
        }
    }

    private String getClaim(String claim) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString(claim);
        }

        return null;
    }
}
