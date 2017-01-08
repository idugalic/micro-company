import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BlogPostsComponent } from './blog-posts.component';
import { BlogPostComponent } from './blog-post/blog-post.component';
import { BlogPostListComponent } from './blog-post-list/blog-post-list.component';
import { BlogPostNewComponent } from './blog-post-new/blog-post-new.component';

const blogPostRoutes: Routes = [
  {
    path: 'blog',
    component: BlogPostsComponent,
    children: [
      {
        path: '',
        component: BlogPostListComponent,
        children: [
          {
            path: ':id',
            component: BlogPostComponent
          },
          {
            path: 'action/new',
            component: BlogPostNewComponent
          }
        ]
      }
    ]
  }
];
@NgModule({
  imports: [
    RouterModule.forChild(blogPostRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class BlogPostsRoutingModule {

}
