package com.example.backend.service;

import com.example.backend.model.DiaryEntity;
import com.example.backend.model.ProfileEntity;
import com.example.backend.repository.DiaryRepository;
import com.example.backend.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProfileService {
    private final String pythonProfilePath;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private DiaryRepository diaryRepository;

    public ProfileService(@Value("${python.profile.path}") String pythonProfilePath) {
        this.pythonProfilePath = pythonProfilePath;
    }

    public List<ProfileEntity> showList(String parentId, String childId) {
        validate(parentId, childId);
        return profileRepository.findByChildId(childId);
    }

    public ProfileEntity show(String parentId, String childId, String profileId) {
        validate(parentId, childId);
        if(!childId.equals(profileRepository.findByProfileId(profileId).getChildId())) {
            log.error("Not the owner of the profile");
            throw new RuntimeException("Not the owner of the profile");
        }
        return profileRepository.findByProfileId(profileId);
    }

    public Resource getImage(String parentId, String childId, String profileId) {
        try {
            ProfileEntity entity = show(parentId, childId, profileId);
            String fileName = entity.getWordCloud();
            Path file = Paths.get(pythonProfilePath + "/images/").resolve(fileName);
            return new UrlResource(file.toUri());
        } catch(Exception e) {
            throw new RuntimeException("Could not read file: ", e);
        }
    }

    public ProfileEntity create(String parentId, String childId) {
        validate(parentId, childId);
        /*if(childRepository.findByChild(childId).getProfileState() >= 5) {

        } else {

        }*/
        String fileName = LocalDateTime.now().toString().replaceAll("[-:.]", "") +
                childId.substring(childId.length() - 5) +
                "_cloud.jpg";
        try {
            Process process = runPythonWordCloud(childId, fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            String errorLine;
            while((errorLine = errorReader.readLine()) != null) {
                System.out.println(errorLine);
            }

            process.waitFor();

            if(process.exitValue() != 0) {
                throw new RuntimeException("Failure for creation wordCloud.");
            }

            ProfileEntity entity = ProfileEntity.builder()
                    .profileId(null)
                    .parentId(parentId)
                    .childId(childId)
                    .wordCloud(fileName)
                    .date(LocalDateTime.now())
                    .build();
            return profileRepository.save(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void validate(String parentId, String childId) {
        /*if(parentId.equals(childRepository.findByChildId(childId).getParentId())) {
            log.error("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }*/
    }

    public Process runPythonWordCloud(String childId, String fileName) throws IOException {
        String pythonWordCloud = pythonProfilePath + "/wordCloud.py";
        return startPythonProcess(pythonWordCloud, childId, fileName);
    }

    public Process startPythonProcess(String pythonWordCloud, String childId, String fileName) throws IOException {
        List<DiaryEntity> diaryList = diaryRepository.findByChildId(childId);

        File diaryFile = new File(pythonProfilePath + "/diary.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(diaryFile))) {
            for (DiaryEntity diary : diaryList) {
                writer.write(diary.getContent());
                writer.newLine();
            }
        }

        List<String> command = new ArrayList<>();
        command.add(pythonProfilePath + "/venv/Scripts/activate.bat");
        command.add("&&");
        command.add("python");
        command.add(pythonWordCloud);
        command.add(diaryFile.getAbsolutePath());
        command.add(fileName);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        return processBuilder.start();
    }
}
