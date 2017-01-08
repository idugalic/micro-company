import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogPostsRoutingModule } from './blog-posts-routing.module';

import { BlogPostsComponent } from './blog-posts.component';
import { BlogPostComponent } from './blog-post/blog-post.component';
import { BlogPostListComponent } from './blog-post-list/blog-post-list.component';
import { ReactiveFormsModule } from '@angular/forms';

import { MaterialModule } from '@angular/material';
import { BlogPostNewComponent } from './blog-post-new/blog-post-new.component';

import { BlogPostsService } from './shared/blog-posts.service';
import { DatePickerModule } from 'ng2-datepicker';

@NgModule({
  imports: [
    CommonModule,
    BlogPostsRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    DatePickerModule
  ],
  declarations: [BlogPostsComponent, BlogPostComponent, BlogPostListComponent, BlogPostNewComponent],
  providers: [BlogPostsService]
})
export class BlogPostsModule { }
