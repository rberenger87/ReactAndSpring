package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exception.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repository.BacklogRepository;
import io.agileintelligence.ppmtool.repository.ProjectRepository;
import io.agileintelligence.ppmtool.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) 
	{
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		if(backlog ==null)
		{
			throw new ProjectNotFoundException("Project not Found");
		}
		
		projectTask.setBacklog(backlog);
		
		Integer backlogSequence = backlog.getPTSequence();
		backlogSequence++;
		backlog.setPTSequence(backlogSequence);
		
		projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		if(projectTask.getPriority() == null || projectTask.getPriority() == 0)
			projectTask.setPriority(3);
		
		if(projectTask.getStatus()=="" || projectTask.getStatus() == null)
			projectTask.setStatus("TO_DO");
		
		return projectTaskRepository.save(projectTask);
	}

	public Iterable<ProjectTask> findBacklogById(String backlog_id) 
	{
		Project project = projectRepository.findByProjectIdentifier(backlog_id);
		
		if(project == null)
			throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}

}
