package com.example.backend.service;

import com.example.backend.model.DiaryEntity;
import com.example.backend.model.ProfileEntity;
import com.example.backend.repository.DiaryRepository;
import com.example.backend.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private DiaryRepository diaryRepository;

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
                    .wordCloud(new File("../python/profile/cloud_images/" + fileName))
                    .date(LocalDateTime.now())
                    .build();
            return profileRepository.save(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProfileEntity> showList(String parentId, String childId) {
        validate(parentId, childId);
        return profileRepository.findByChildId(childId);
    }

    public void validate(String parentId, String childId) {
        /*if(parentId.equals(childRepository.findByChildId(childId).getParentId())) {
            log.error("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }*/
    }

    public Process runPythonWordCloud(String childId, String fileName) throws IOException {
        String pythonWordCloud = "../python/profile/wordCloud.py";
        return startPythonProcess(pythonWordCloud, childId, fileName);
    }

    public Process startPythonProcess(String pythonWordCloud, String childId, String fileName) throws IOException {
        List<DiaryEntity> diaryList = diaryRepository.findByChildId(childId);

        File diaryFile = new File("../python/profile/diary.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(diaryFile))) {
            for (DiaryEntity diary : diaryList) {
                writer.write(diary.getContent());
                writer.newLine();
            }
        }

        List<String> command = new ArrayList<>();
        command.add("../python/profile/venv/Scripts/activate.bat");
        command.add("&&");
        command.add("python");
        command.add(pythonWordCloud);
        command.add(diaryFile.getAbsolutePath());
        command.add(fileName);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        return processBuilder.start();
    }
}
