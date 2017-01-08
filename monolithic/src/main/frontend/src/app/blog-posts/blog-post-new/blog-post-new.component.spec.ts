/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { MaterialModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';
import { BlogPostsService } from '../shared/blog-posts.service';
import { EventManager } from '../../shared/event-manager.service';
import { DatePickerModule } from 'ng2-datepicker';

import { BlogPostNewComponent } from './blog-post-new.component';

describe('BlogPostNewComponent', () => {
  let component: BlogPostNewComponent;
  let fixture: ComponentFixture<BlogPostNewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlogPostNewComponent ],
      imports: [ RouterTestingModule, MaterialModule, ReactiveFormsModule, DatePickerModule ],
      providers: [BlogPostsService, EventManager]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogPostNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
