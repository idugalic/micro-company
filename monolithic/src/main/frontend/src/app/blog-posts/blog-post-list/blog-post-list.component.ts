import { Component, OnInit } from '@angular/core';
import { BlogPostsService } from '../shared/blog-posts.service';
import { BlogPostModel } from '../shared/blog-post.model';
import { EventManager } from '../../shared/event-manager.service';

@Component({
  selector: 'app-blog-post-list',
  templateUrl: './blog-post-list.component.html',
  styleUrls: ['./blog-post-list.component.css']
})
export class BlogPostListComponent implements OnInit {

  blogPosts: BlogPostModel[];
  errorMessage: string;

  constructor(private blogPostsService: BlogPostsService, private eventManager: EventManager) {  }

  ngOnInit(): void {
    this.getBlogPosts();
    this.registerChange();
  }

  registerChange() {
      this.eventManager.subscribe('blogPostListModification', (response) => this.getBlogPosts());
  }

  getBlogPosts(): void {
    this.blogPostsService.getBlogPosts().subscribe(
                                      blogPosts => this.blogPosts = blogPosts,
                                      error =>  this.errorMessage = <any>error);
  }
}
