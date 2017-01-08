import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectsComponent } from './projects.component';
import { ProjectComponent } from './project/project.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectsRoutingModule } from './projects-routing.module';
import { MaterialModule } from '@angular/material';
import { ProjectsService } from './shared/projects.service';
import { ProjectNewComponent } from './project-new/project-new.component';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  imports: [
    CommonModule,
    ProjectsRoutingModule,
    MaterialModule,
    ReactiveFormsModule
  ],
  declarations: [ProjectsComponent, ProjectComponent, ProjectListComponent, ProjectNewComponent],
  providers: [ProjectsService]
})
export class ProjectsModule { }
