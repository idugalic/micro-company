/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { MaterialModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';

import { ProjectNewComponent } from './project-new.component';
import { ProjectsService } from '../shared/projects.service';
import { EventManager } from '../../shared/event-manager.service';

describe('ProjectNewComponent', () => {
  let component: ProjectNewComponent;
  let fixture: ComponentFixture<ProjectNewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectNewComponent ],
      imports: [ RouterTestingModule, MaterialModule, ReactiveFormsModule],
      providers: [ProjectsService, EventManager]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
