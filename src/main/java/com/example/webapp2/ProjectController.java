package com.example.webapp2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webapp2.Project;
import com.example.webapp2.UserRepository;
import com.example.webapp2.ProjectRepository;

@CrossOrigin(origins = "http://localhost:8084")
@RestController
@RequestMapping("/api")
public class ProjectController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<Project>> getAllProjectsByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id = " + userId);
        }

        List<Project> projects = projectRepository.findByUserId(userId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectsByUserId(@PathVariable(value = "id") Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<Project> createProject(@PathVariable(value = "userId") Long userId,
                                                 @RequestBody Project projectRequest) {
        Project project = userRepository.findById(userId).map(user -> {
            projectRequest.setUser(user);
            return projectRepository.save(projectRequest);
        })
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + userId));

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") long id, @RequestBody Project projectRequest) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));

        project.setDescription(projectRequest.getDescription());

        return new ResponseEntity<>(projectRepository.save(project), HttpStatus.OK);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable("id") long id) {
        projectRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/users/{userId}/projects")
    public ResponseEntity<List<Project>> deleteAllProjectsOfUser(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id = " + userId);
        }

        projectRepository.deleteByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
