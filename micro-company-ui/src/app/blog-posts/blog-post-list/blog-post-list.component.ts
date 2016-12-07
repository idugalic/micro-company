import { Component, OnInit } from '@angular/core';
import { BlogPostsService } from '../shared/blog-posts.service';
import { BlogPostModel } from '../shared/blog-post.model';

@Component({
  selector: 'app-blog-post-list',
  templateUrl: './blog-post-list.component.html',
  styleUrls: ['./blog-post-list.component.css'],
  providers: [BlogPostsService]
})
export class BlogPostListComponent implements OnInit {

  blogPosts: BlogPostModel[];
  errorMessage: string;

  constructor(blogPostsService: BlogPostsService) {
    blogPostsService.getBlogPosts().subscribe(
                                      blogPosts => this.blogPosts = blogPosts,
                                      error =>  this.errorMessage = <any>error);
  }

  ngOnInit() {
  }

}
