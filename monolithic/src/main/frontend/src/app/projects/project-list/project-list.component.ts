import { Component, OnInit } from '@angular/core';
import { ProjectsService } from '../shared/projects.service';
import { ProjectModel } from '../shared/project.model';
import { EventManager } from '../../shared/event-manager.service';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

  projects: ProjectModel[];
  errorMessage: string;

  constructor(private projectsService: ProjectsService, private eventManager: EventManager) { }

  ngOnInit(): void {
    this.getProjects();
    this.registerChange();
  }

  registerChange() {
      this.eventManager.subscribe('projectListModification', (response) => this.getProjects());
  }

  private getProjects(): void {
    this.projectsService.getProjects().subscribe(
                                      projects => this.projects = projects,
                                      error =>  this.errorMessage = <any>error);
  }

}
