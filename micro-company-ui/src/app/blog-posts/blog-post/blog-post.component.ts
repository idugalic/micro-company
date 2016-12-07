import { Component, OnInit } from '@angular/core';
import { BlogPostsService } from '../shared/blog-posts.service';
import { BlogPostModel } from '../shared/blog-post.model';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'app-blog-post',
  templateUrl: './blog-post.component.html',
  styleUrls: ['./blog-post.component.css'],
  providers: [BlogPostsService]
})
export class BlogPostComponent implements OnInit {

  blogPost: BlogPostModel;
  errorMessage: string;
  navigated = false;

  constructor(
    private blogPostsService: BlogPostsService,
    private route: ActivatedRoute,
    private router: Router
  ) {  }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      if (params['id'] !== undefined) {
        let id = params['id'];
        this.navigated = true;
        this.getBlogPost(id);
      } else {
        this.navigated = false;
        this.blogPost = new BlogPostModel();
      }
     });
    }

  private getBlogPost(id: string): void {
    this.blogPostsService.getBlogPost(id).subscribe(
                                      blogPost => this.blogPost = blogPost,
                                      error =>  this.errorMessage = <any>error);
  }

}
