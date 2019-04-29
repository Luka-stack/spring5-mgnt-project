package com.nisshoku.mgnt.bootstrap;

import com.nisshoku.mgnt.domain.*;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DataLoader(EmployeeRepository employeeRepository, ProjectRepository projectRepository,
                      TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Taka");
        newEmployee.setLastName("Hayami");
        newEmployee.setFavoriteLanguage(Language.GO);
        newEmployee.setEmail("Taka@nisshoku.jp");
        newEmployee.setPhoneNumber("123456789");

        Project newProject = new Project();
        newProject.setTitle("Project");
        newProject.setDescription("Desc Of Project");
        newProject.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01"));
        newProject.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-01"));
        newProject.setStateOfProject(State.DONE);
        newProject.setCost(100.09);

        Project newProject2 = new Project();
        newProject2.setTitle("Project _ 2");
        newProject2.setDescription("Desc Of Project _ 2");
        newProject2.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-10"));
        newProject2.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-25"));
        //newProject2.setStartDate(LocalDate.now());
        //newProject2.setEndDate(LocalDate.now());
        newProject2.setStateOfProject(State.IN_PROGRESS);
        newProject2.setCost(1000.09);

        Task newTask = new Task();
        newTask.setTitle("Title Task");
        newTask.setDescription("Desc For Task");
        newTask.setStateOfTask(State.REJECTED);

        // Relationships
        // Project - Task
        //newTask.setProject(newProject);
        //newProject.getTasks().add(newTask);
        // Employee - Project
        newEmployee.getProjects().add(newProject);
        newEmployee.getProjects().add(newProject2);
        newProject.getEmployees().add(newEmployee);
        newProject2.getEmployees().add(newEmployee);

        employeeRepository.save(newEmployee);
        projectRepository.save(newProject);
        projectRepository.save(newProject2);
        taskRepository.save(newTask);

        Employee newEmployee2 = new Employee();
        newEmployee2.setFirstName("Ryuki");
        newEmployee2.setLastName("Rafa");
        newEmployee2.setFavoriteLanguage(Language.PYTHON);
        newEmployee2.setEmail("Ryuki@nisshoku.jp");
        newEmployee2.setPhoneNumber("987654321");

        Employee newEmployee3 = new Employee();
        newEmployee3.setFirstName("Rose");
        newEmployee3.setLastName("Uzumaki");
        newEmployee3.setFavoriteLanguage(Language.Cpp);
        newEmployee3.setEmail("Rose@nisshoku.jp");
        newEmployee3.setPhoneNumber("135798642");

        Project newProject3 = new Project();
        newProject3.setTitle("Super Project");
        newProject3.setDescription("New Super Project");
        newProject3.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-10"));
        //newProject3.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-29"));
        newProject3.setEndDate(new SimpleDateFormat("yyyy").parse("2019"));
        //newProject3.setStartDate(LocalDate.now());
        //newProject3.setEndDate(LocalDate.now());
        newProject3.setStateOfProject(State.IN_PROGRESS);
        newProject3.setCost(909.09);

        Task newTask2 = new Task();
        newTask2.setTitle("Easy Task");
        newTask2.setDescription("Very Easy Task");
        newTask2.setStateOfTask(State.BUG);

        Task newTask3 = new Task();
        newTask3.setTitle("Hard Task");
        newTask3.setDescription("Rather Hard Task");
        newTask3.setStateOfTask(State.ACCEPTED);

        // Relationships
        // Project - Task
        //newTask2.setProject(newProject2);
        //newTask3.setProject(newProject2);
        //newProject2.getTasks().add(newTask2);
        //newProject2.getTasks().add(newTask3);
        // Employee - Project
        newEmployee2.getProjects().add(newProject3);
        newEmployee3.getProjects().add(newProject3);
        newProject3.getEmployees().add(newEmployee2);
        newProject3.getEmployees().add(newEmployee3);

        employeeRepository.save(newEmployee2);
        employeeRepository.save(newEmployee3);
        projectRepository.save(newProject3);
        taskRepository.save(newTask2);
        taskRepository.save(newTask3);
    }
}
