package com.example.demo.service;

import com.example.demo.exception.InvalidImageInputException;
import com.example.demo.exception.PythonException;
import com.example.demo.model.GlobalPathConstants;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void newUser(@NonNull String filename, @NonNull Image image){
        String [] pythonCmd = new String[3];
        pythonCmd[0] = "python";
        pythonCmd[1] = GlobalPathConstants.PYTHON_NEW_USER_SCRIPT;
        pythonCmd[2] = GlobalPathConstants.USER_PATH + filename;
        try {
            int exitCode = Runtime.getRuntime().exec(pythonCmd).waitFor();
            switch (exitCode) {
                case -1:
                    throw new PythonException("Image path is empty or doesn't exist.");
                case -2:
                    throw new InvalidImageInputException("The picture has no valid face or more than 1 valid face.");
                case 0:
                    break;
                default:
                    throw new PythonException("Failed to run the python script.");
            }
            User user = new User();
            user.setName(FilenameUtils.getBaseName(filename));
            user.setImage(image);
            user.setEntries(new HashSet<>());
            userRepo.save(user);
        } catch (IOException | InterruptedException e) {
            throw new PythonException("Failed to run the python script.", e);
        }
    }
}
