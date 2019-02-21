package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exception.ProjectIdException;
import io.agileintelligence.ppmtool.repository.BacklogRepository;
import io.agileintelligence.ppmtool.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;

	public Project saveOrUpdate(Project project) {
		
		String projectIdentifier = project.getProjectIdentifier().toUpperCase();
		try {
			project.setProjectIdentifier(projectIdentifier);
			
			if(project.getId()==null)
			{
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(projectIdentifier);
			}
			
			if (project.getId()!= null)
			{
				project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project ID '"+projectIdentifier+"' already exists");
		}
		
		
	}
	
	public Project findProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project ID '"+projectId.toUpperCase()+"' does not exist");
		}
		
		return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Cannot delete project with ID '"+projectId.toUpperCase()+"'. This project does not exist");
		}
		
		projectRepository.delete(project);
		
	}
	
}
