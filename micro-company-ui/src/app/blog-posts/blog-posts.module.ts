import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogPostsComponent } from './blog-posts.component';
import { BlogPostComponent } from './blog-post/blog-post.component';
import { BlogPostListComponent } from './blog-post-list/blog-post-list.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [BlogPostsComponent, BlogPostComponent, BlogPostListComponent]
})
export class BlogPostsModule { }
