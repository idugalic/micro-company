import { Injectable }     from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { BlogPostModel } from './blog-post.model';
import { Observable }     from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class BlogPostsService {
  private blogPostQueryBaseUrl = '/api/blogposts';
  private blogPostCommandBaseUrl = '/api/blogpostcommands';

  constructor (private http: Http) {}

  private extractListData(res: Response) {
    let body = res.json();
    return body._embedded.blogposts || { };
  }
  private extractSingleData(res: Response) {
    let body = res.json();
    return body || { };
  }

  public getBlogPosts (): Observable<BlogPostModel[]> {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.get(this.blogPostQueryBaseUrl, options)
                    .map(this.extractListData);
  }

  public getBlogPost(id: string): Observable<BlogPostModel> {
    const url = `${this.blogPostQueryBaseUrl}/${id}`;
    return this.http.get(url)
                  .map(this.extractSingleData);
}

public addBlogPost(post: BlogPostModel): Observable<any> {
  let headers = new Headers({ 'Content-Type': 'application/json' });
  let options = new RequestOptions({ headers: headers });

  return this.http.post(this.blogPostCommandBaseUrl, post , options);
}

}
