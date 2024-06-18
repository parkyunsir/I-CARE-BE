package com.example.backend.service;

import com.example.backend.model.DiaryEntity;
import com.example.backend.model.IconEntity;
import com.example.backend.repository.IconRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IconService {
    @Autowired
    private IconRepository iconRepository;

    public List<IconEntity> offerList(String content) {
        return iconRepository.findAll();
    }

    public IconEntity show(Long iconId) {
        return iconRepository.findByIconId(iconId);
    }
/*
    public List<IconEntity> offerList(String content) {
        try {
            Process process = runPythonWordCloud(content);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while(reader.readLine() != null) {
                line = reader.readLine();
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

            List<IconEntity> entities;
            //entities.add(0, iconRepository.findByIconId(line));
            //return entities;
            return iconRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Process runPythonWordCloud(String content) throws IOException {
        String pythonWordCloud = "../python/icon" + "/icon_temporary.py";
        return startPythonProcess(pythonWordCloud, content);
    }

    public Process startPythonProcess(String pythonWordCloud, String content) throws IOException {
        List<String> command = new ArrayList<>();
        //command.add(pythonProfilePath + "/venv/Scripts/activate.bat");
        // command.add("&&");
        command.add("python");
        command.add(pythonWordCloud);
        command.add(content);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        return processBuilder.start();
    }*/
}
