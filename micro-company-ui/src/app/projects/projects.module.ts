import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectsComponent } from './projects.component';
import { ProjectComponent } from './project/project.component';
import { ProjectListComponent } from './project-list/project-list.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [ProjectsComponent, ProjectComponent, ProjectListComponent]
})
export class ProjectsModule { }
