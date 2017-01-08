import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BlogPostsService } from '../shared/blog-posts.service';
import { FormGroup, Validators, FormBuilder, FormControl } from '@angular/forms';
import { EventManager } from '../../shared/event-manager.service';
import { BlogPostModel} from '../shared/blog-post.model';



@Component({
  selector: 'app-blog-post-new',
  templateUrl: './blog-post-new.component.html',
  styleUrls: ['./blog-post-new.component.css']

})
export class BlogPostNewComponent implements OnInit {

   form: FormGroup;
   isSaving: Boolean;

  constructor(
    private blogPostsService: BlogPostsService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private eventManager: EventManager
  ) {}

  ngOnInit() {
    this.form = new FormGroup({
    title: new FormControl(''),
    rawContent: new FormControl(''),
    publicSlug: new FormControl(''),
    draft: new FormControl(true),
    category: new FormControl(''),
    broadcast: new FormControl(true),
    publishAt: new FormControl('')
  });
  }
  onSubmit({ value, valid }: { value: BlogPostModel, valid: boolean }) {
      let ngbDate = this.form.controls['publishAt'].value;
      value.publishAt = new Date(ngbDate.year, ngbDate.month - 1, ngbDate.day);
      this.blogPostsService.addBlogPost(value).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'blogPostListModification', content: 'OK' });
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
