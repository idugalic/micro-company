import { Component, OnInit } from '@angular/core';
import { ProjectsService } from '../shared/projects.service';
import { ProjectModel } from '../shared/project.model';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  providers: [ProjectsService]
})
export class ProjectListComponent implements OnInit {

  projects: ProjectModel[];
  errorMessage: string;

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.getProjects();
  }

  private getProjects(): void {
    this.projectsService.getProjects().subscribe(
                                      projects => this.projects = projects,
                                      error =>  this.errorMessage = <any>error);
  }

}
