import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ProjectsService } from '../shared/projects.service';
import { ProjectModel } from '../shared/project.model';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css'],
  providers: [ProjectsService]
})
export class ProjectComponent implements OnInit {

  project: ProjectModel;
  errorMessage: string;
  navigated = false;

  constructor(
    private projectsService: ProjectsService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      if (params['id'] !== undefined) {
        let id = params['id'];
        this.navigated = true;
        this.getProject(id);
      } else {
        this.navigated = false;
        this.project = new ProjectModel();
      }
     });
    }

  private getProject(id: string): void {
    this.projectsService.getProject(id).subscribe(
                                      project => this.project = project,
                                      error =>  this.errorMessage = <any>error);
  }

}
